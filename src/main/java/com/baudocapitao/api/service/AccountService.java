package com.baudocapitao.api.service;

import com.baudocapitao.api.enums.AccountType;
import com.baudocapitao.api.model.Account;
import com.baudocapitao.api.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(BigDecimal.ZERO);
        }
        account.setCreatedAt(LocalDateTime.now());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAccountsByUserId(String userId) {
        return accountRepository.findByUserId(userId);
    }

    public Optional<Account> getAccountById(String id) {
        if (id == null) {
            return Optional.empty();
        }
        return accountRepository.findById(id);
    }

    public Account updateAccount(String id, Account accountDetails) {
        if (id == null || accountDetails == null) {
            throw new IllegalArgumentException("ID e accountDetails não podem ser nulos");
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada com id: " + id));
        account.setName(accountDetails.getName());
        account.setType(accountDetails.getType());
        account.setBalance(accountDetails.getBalance());
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }

    public void deleteAccount(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID e accountDetails não podem ser nulos");
        }
        accountRepository.deleteById(id);
    }

    public Account updateBalance(String id, BigDecimal newBalance) {
        if (id == null) {
            throw new IllegalArgumentException("ID e accountDetails não podem ser nulos");
        }
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));
        account.setBalance(newBalance);
        account.setUpdatedAt(LocalDateTime.now());
        return accountRepository.save(account);
    }
    
    public Page<Account> listAccounts(String userId, AccountType type, Pageable pageable) {
        if (type != null) {
            return accountRepository.findByUserIdAndType(userId, type, pageable);
        } else {
            return accountRepository.findByUserId(userId, pageable);
        }
    }
}