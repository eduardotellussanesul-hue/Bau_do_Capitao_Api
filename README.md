# 🏴‍☠️ Bau do Capitão – API de Controle Financeiro

Bem-vindo ao projeto Bau do Capitão! Esta é uma API REST desenvolvida em Java com Spring Boot que servirá como backend para um sistema de finanças pessoais. Atualmente, o projeto possui a estrutura base configurada, um endpoint de exemplo (/api/hello) e documentação interativa via Swagger.

## 📌 Índice

- Sobre o projeto
- Tecnologias utilizadas
- Pré‑requisitos (Windows e Linux)
- Como executar o projeto (clone + execução)
- Acessar o Swagger
- Estrutura do projeto
- Detalhamento do que foi feito até agora
- Comandos úteis
- Próximos passos
- Licença

## 🧭 Sobre o projeto

O **Bau do Capitão** é uma API que permitirá:

- Registrar receitas e despesas
- Categorizar transações
- Visualizar saldo e relatórios
- Definir orçamentos mensais

Atualmente, temos a **estrutura base** (Spring Boot com Gradle) e um endpoint de exemplo para testar o funcionamento. A documentação interativa (Swagger) já está configurada e acessível via navegador.
---

## 🧰 Tecnologias utilizadas

- **Java 17** (LTS)
- **Spring Boot 3.2.2**
- **Gradle** (Wrapper incluso – você não precisa instalar)
- **SpringDoc OpenAPI 2.5.0** (Swagger UI)

---
---
### ✅ Windows
1° Primeiro, instale o Java 17 se não tiver. 
2° Baixe o instalador do Eclipse Temurin (OpenJDK) em https://adoptium.net/temurin/releases/?version=17, escolha o arquivo .msi para Windows (x64). 
3° Após a instalação, feche e reabra o PowerShell e verifique com o comando: java -version. 
4° Em seguida, instale o Spring Boot CLI via Chocolatey. 
5° Abra o PowerShell como Administrador e execute  choco install spring-boot-cli -y.


### ✅  Linux (Ubuntu/Debian)
1° Instale o Java 17 com os comandos: 
2° sudo apt update; 
3sudo apt install openjdk-17-jdk -y; 
java -version. 
Depois, instale o Spring Boot CLI via SDKMAN: 
curl -s "https://get.sdkman.io" | bash; source "$HOME/.sdkman/bin/sdkman-init.sh"; 
sdk install springboot. 


## Para rodar a aplicação, utilize o Gradle Wrapper – você não precisa instalar o Gradle.  
### No Windows (PowerShell): 
Set-ExecutionPolicy -ExecutionPolicy RemoteSigned -Scope CurrentUser (apenas se necessário); 
.\gradlew.bat bootRun. 

### No Linux (terminal): 
./gradlew bootRun. 
A primeira execução pode demorar alguns minutos enquanto o Gradle baixa as dependências. Q
uando a aplicação iniciar, você verá uma mensagem como "Started BauDoCapitaoApiApplication in X seconds".

## Acessar o Swagger (documentação da API): 

Com a aplicação rodando, abra o navegador em http://localhost:8080/swagger-ui.html. 
Lá você encontrará todos os endpoints disponíveis, incluindo o GET /api/hello. 
Você pode testar as requisições diretamente pela interface. 
A documentação JSON (OpenAPI) também está disponível em http://localhost:8080/v3/api-docs.

## Estrutura do projeto: 
Bau_do_Capitao_Api/ 
|-- src/ 
|---- main/ 
|------ java/com/baudocapitao/api/ 
|-------- BauDoCapitaoApiApplication.java (classe principal) 
|-------- controller/ 
|---------- HelloController.java (endpoint de exemplo) 
|-------- config/ 
|---------- OpenApiConfig.java (configuração do Swagger) 
|------ resources/ 
|-------- application.properties 
|-------- static/ 
|-------- templates/ 
|-- build.gradle (dependências) 
|-- settings.gradle 
|-- gradlew (Linux/Mac) / gradlew.bat (Windows) (Wrapper do Gradle) 
|-- .gitignore |-- README.md

