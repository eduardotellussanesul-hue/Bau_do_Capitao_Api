# 🏴‍☠️ Bau do Capitão – API de Controle Financeiro

API REST para gerenciamento financeiro pessoal, construída com Spring Boot e MongoDB. Oferece cadastro de usuários, contas, categorias e transações, com suporte a parcelamento, atualização automática de saldos, paginação, filtros e documentação Swagger.

---

## 📌 Índice

- [Visão Geral](#visão-geral)
- [Modelo de Dados](#modelo-de-dados)
- [Arquitetura e Decisões Técnicas](#arquitetura-e-decisões-técnicas)
- [Endpoints da API](#endpoints-da-api)
  - [Usuários](#usuários)
  - [Contas](#contas)
  - [Categorias](#categorias)
  - [Transações](#transações)
- [Configuração e Execução](#configuração-e-execução)
- [Tecnologias Utilizadas](#tecnologias-utilizadas)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Próximos Passos](#próximos-passos)

---

## Visão Geral

O **Bau do Capitão** é uma API REST que serve como backend para um sistema de finanças pessoais. Ela foi projetada para ser consumida por aplicações web ou mobile, oferecendo:

- Gestão de usuários
- Múltiplas contas por usuário (corrente, poupança, cartão de crédito, dinheiro)
- Categorias de transações (globais e personalizadas)
- Lançamento de receitas e despesas com parcelamento
- Atualização automática do saldo das contas
- Listagens paginadas e filtradas
- Documentação interativa via Swagger UI

**Principais funcionalidades implementadas:**

- CRUD completo para todas as entidades
- Transações parceladas (criação em lote)
- Reversão de saldo ao atualizar/excluir transações
- Filtros por nome, email, tipo, data, conta, categoria
- Ordenação por qualquer campo
- DTOs para respostas enxutas e enriquecidas

---

## Modelo de Dados

Abaixo estão as entidades principais e seus relacionamentos.

### Entidades

#### Usuário (`User`)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | String (ObjectId) | Identificador único |
| `name` | String | Nome do usuário |
| `email` | String | E-mail único |
| `passwordHash` | String | Hash da senha |
| `createdAt` | LocalDateTime | Data de criação |
| `updatedAt` | LocalDateTime | Última atualização |

#### Conta (`Account`)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | String | Identificador único |
| `userId` | String | Referência ao usuário dono |
| `name` | String | Nome da conta (ex: "Nubank") |
| `type` | Enum | `CHECKING`, `SAVINGS`, `CREDIT_CARD`, `CASH` |
| `balance` | BigDecimal | Saldo atual |
| `createdAt`, `updatedAt` | LocalDateTime | Auditoria |

#### Categoria (`Category`)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | String | Identificador |
| `userId` | String ou null | Se null, é global; senão, pessoal |
| `name` | String | Nome da categoria |
| `type` | Enum | `INCOME` ou `EXPENSE` |
| `icon` | String | Emoji opcional |
| `isGlobal` | Boolean | Indica se é global |
| `createdAt`, `updatedAt` | LocalDateTime | Auditoria |

#### Transação (`Transaction`)
| Campo | Tipo | Descrição |
|-------|------|-----------|
| `id` | String | Identificador |
| `userId` | String | Usuário dono |
| `accountId` | String | Conta associada |
| `categoryId` | String ou null | Categoria (opcional) |
| `description` | String | Descrição do lançamento |
| `amount` | BigDecimal | Valor (positivo) |
| `type` | Enum | `INCOME` ou `EXPENSE` |
| `date` | LocalDateTime | Data da transação |
| `paymentMethod` | Enum | `CASH`, `CREDIT`, `DEBIT`, `PIX`, `TRANSFER` |
| `isRecurring` | Boolean | Se é recorrente |
| `parentTransactionId` | String | Auto-relacionamento para parcelas |
| `installmentNumber` | Integer | Número da parcela (1..N) |
| `totalInstallments` | Integer | Total de parcelas |
| `createdAt`, `updatedAt` | LocalDateTime | Auditoria |

### Relacionamentos

- `Account.userId` → `User.id`
- `Category.userId` → `User.id` (pode ser nulo para globais)
- `Transaction.userId` → `User.id`
- `Transaction.accountId` → `Account.id`
- `Transaction.categoryId` → `Category.id`
- `Transaction.parentTransactionId` → `Transaction.id` (auto-relacionamento)

---

## Arquitetura e Decisões Técnicas

### Camadas (Clean Architecture)

- **Controller**: Responsável por receber requisições HTTP, validar parâmetros e chamar os serviços. Retorna DTOs.
- **Service**: Contém toda a lógica de negócio (cálculo de saldos, parcelamento, validações). Transacional.
- **Repository**: Interface com MongoDB (Spring Data). Usa `MongoTemplate` para queries dinâmicas (transações).
- **Model**: Entidades JPA-like (documentos MongoDB).
- **DTO**: Objetos de transferência para respostas da API, evitando expor dados internos.
- **Mapper**: Classe centralizada que converte `Model` → `DTO` para todas as entidades.

### Destaques

- **Paginação e Filtros**: Endpoints como `/api/users/pagination`, `/api/transactions/paged` usam `Pageable` do Spring Data, com parâmetros `page`, `size`, `sort`. Filtros opcionais (nome, email, data, etc.) são aplicados dinamicamente.
- **Atualização Automática de Saldo**: Ao criar/atualizar/excluir uma transação, o saldo da conta é recalculado automaticamente (reversão + nova aplicação).
- **Parcelamento**: O endpoint `/api/transactions/installments` recebe uma transação raiz e gera N parcelas (com valores iguais), vinculadas via `parentTransactionId`.
- **Enriquecimento de DTO**: `TransactionResponseDTO` inclui `accountName` e `categoryName`, obtidos via consultas em lote para evitar N+1.
- **Categorias Globais**: O sistema pré-carrega categorias padrão (ex: "Salário", "Alimentação") que são visíveis para todos os usuários, mas podem ser complementadas com categorias pessoais.

---

## Endpoints da API

Todos os endpoints são prefixados com `/api`. A documentação completa (com exemplos) está disponível no Swagger: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html).

### Usuários

| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | `/users` | Criar novo usuário |
| GET | `/users` | Listar todos (sem paginação) |
| GET | `/users/pagination` | Listar com paginação e filtros (`name`, `email`) |
| GET | `/users/{id}` | Buscar por ID |
| PUT | `/users/{id}` | Atualizar usuário |
| DELETE | `/users/{id}` | Deletar usuário |

### Contas

| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | `/accounts` | Criar nova conta |
| GET | `/accounts` | Listar paginado (obrigatório `userId`, opcional `type`) |
| GET | `/accounts/{id}` | Buscar por ID |
| PUT | `/accounts/{id}` | Atualizar conta |
| DELETE | `/accounts/{id}` | Deletar conta |

### Categorias

| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | `/categories` | Criar categoria (global ou pessoal) |
| GET | `/categories` | Listar paginado (opcionais `userId`, `type`) – retorna globais + pessoais se `userId` informado |
| GET | `/categories/global` | Listar apenas globais |
| GET | `/categories/{id}` | Buscar por ID |
| PUT | `/categories/{id}` | Atualizar categoria |
| DELETE | `/categories/{id}` | Deletar categoria |

### Transações

| Método | Caminho | Descrição |
|--------|---------|-----------|
| POST | `/transactions` | Criar transação simples (atualiza saldo) |
| POST | `/transactions/installments?totalInstallments=N` | Criar transação parcelada (gera N parcelas) |
| GET | `/transactions/paged` | Listar paginado com filtros (`userId` obrigatório; opcionais: `accountId`, `categoryId`, `type`, `startDate`, `endDate`) |
| GET | `/transactions/account/{accountId}` | Listar transações de uma conta |
| GET | `/transactions/user/{userId}/period?start=...&end=...` | Listar transações de um usuário em um período |
| GET | `/transactions/{id}` | Buscar por ID |
| PUT | `/transactions/{id}` | Atualizar transação (reverte saldo antigo e aplica novo) |
| DELETE | `/transactions/{id}` | Deletar transação (reverte saldo) |

**Parâmetros de paginação comuns a todos os endpoints paginados:**
- `page` (padrão 0) – número da página (base 0)
- `size` (padrão 10) – itens por página
- `sort` (opcional) – campo e direção, ex: `sort=date,desc`

---

## Configuração e Execução

### Pré‑requisitos

- Java 17+
- MongoDB (local ou Atlas)
- Git (para clonar)

### Passos

1. Clone o repositório:
   ```bash
   git clone <url>
   cd Bau_do_Capitao_Api

2. Configure o MongoDB no arquivo src/main/resources/application.properties:

properties
spring.data.mongodb.uri=mongodb+srv://<usuario>:<senha>@<cluster>/?retryWrites=true&w=majority
spring.data.mongodb.database=bau_do_capitao

3. Execute a aplicação:

bash
./gradlew bootRun        # Linux/Mac
gradlew.bat bootRun      # Windows