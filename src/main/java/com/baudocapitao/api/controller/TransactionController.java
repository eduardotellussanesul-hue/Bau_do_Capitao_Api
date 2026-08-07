package com.baudocapitao.api.controller;

import com.baudocapitao.api.dto.TransactionResponseDTO;
import com.baudocapitao.api.enums.TransactionType;
import com.baudocapitao.api.model.Transaction;
import com.baudocapitao.api.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "CRUD de transações financeiras")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    @Operation(summary = "Cria uma nova transação")
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Transaction saved = transactionService.createTransaction(transaction);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @PostMapping("/installments")
    @Operation(summary = "Cria uma transação com parcelas")
    public ResponseEntity<List<Transaction>> createInstallments(
            @RequestBody Transaction parent,
            @RequestParam int totalInstallments) {
        List<Transaction> installments = transactionService.createInstallments(parent, totalInstallments);
        return new ResponseEntity<>(installments, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todas as transações (opcional filtrar por usuário)")
    public List<Transaction> getAllTransactions(@RequestParam(required = false) String userId) {
        if (userId != null) {
            return transactionService.getTransactionsByUser(userId);
        }
        return transactionService.getAllTransactions();
    }

    @GetMapping("/account/{accountId}")
    @Operation(summary = "Busca transações por conta")
    public List<Transaction> getTransactionsByAccount(@PathVariable String accountId) {
        return transactionService.getTransactionsByAccount(accountId);
    }

    @GetMapping("/user/{userId}/period")
    @Operation(summary = "Busca transações de um usuário em um período")
    public List<Transaction> getTransactionsByUserAndPeriod(
            @PathVariable String userId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end) {
        return transactionService.getTransactionsByUserAndDateRange(userId, start, end);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma transação pelo ID")
    public ResponseEntity<Transaction> getTransactionById(@PathVariable String id) {
        return transactionService.getTransactionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma transação existente")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable String id, @RequestBody Transaction transaction) {
        try {
            Transaction updated = transactionService.updateTransaction(id, transaction);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma transação pelo ID")
    public ResponseEntity<Void> deleteTransaction(@PathVariable String id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/pagination")
    @Operation(summary = "Lista transações com paginação e filtros (com nomes de conta/categoria)")
    public Page<TransactionResponseDTO> listTransactions(
            @RequestParam String userId,
            @RequestParam(required = false) String accountId,
            @RequestParam(required = false) String categoryId,
            @RequestParam(required = false) TransactionType type,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            Pageable pageable) {

        return transactionService.listTransactionsWithDetails(
                userId, accountId, categoryId, type, startDate, endDate, pageable);
    }
}