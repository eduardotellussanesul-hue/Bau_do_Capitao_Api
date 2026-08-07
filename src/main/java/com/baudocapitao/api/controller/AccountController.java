package com.baudocapitao.api.controller;

import com.baudocapitao.api.dto.AccountResponseDTO;
import com.baudocapitao.api.enums.AccountType;
import com.baudocapitao.api.mapper.Mapper;
import com.baudocapitao.api.model.Account;
import com.baudocapitao.api.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@Tag(name = "Accounts", description = "CRUD de contas financeiras")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    @Operation(summary = "Cria uma nova conta")
    public ResponseEntity<Account> createAccount(@RequestBody Account account) {
        Account saved = accountService.createAccount(account);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todas as contas")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Busca contas por ID do usuário")
    public List<Account> getAccountsByUser(@PathVariable String userId) {
        return accountService.getAccountsByUserId(userId);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma conta pelo ID")
    public ResponseEntity<Account> getAccountById(@PathVariable String id) {
        return accountService.getAccountById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma conta existente")
    public ResponseEntity<Account> updateAccount(@PathVariable String id, @RequestBody Account account) {
        try {
            Account updated = accountService.updateAccount(id, account);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma conta pelo ID")
    public ResponseEntity<Void> deleteAccount(@PathVariable String id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pagination")
    @Operation(summary = "Lista contas com paginação e filtros")
    public Page<AccountResponseDTO> listAccounts(
            @RequestParam String userId,
            @RequestParam(required = false) AccountType type,
            Pageable pageable) {
        return accountService.listAccounts(userId, type, pageable)
                .map(mapper::toAccountResponseDTO);
    }
}