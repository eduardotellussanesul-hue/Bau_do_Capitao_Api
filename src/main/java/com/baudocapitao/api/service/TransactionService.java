package com.baudocapitao.api.service;

import com.baudocapitao.api.model.Account;
import com.baudocapitao.api.model.Category;
import com.baudocapitao.api.model.Transaction;
import com.baudocapitao.api.repository.AccountRepository;
import com.baudocapitao.api.repository.CategoryRepository;
import com.baudocapitao.api.repository.TransactionRepository;
import com.baudocapitao.api.dto.TransactionResponseDTO;
import com.baudocapitao.api.enums.TransactionType;
import com.baudocapitao.api.mapper.Mapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private AccountService accountService; 

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private Mapper mapper;

    @Transactional
    public Transaction createTransaction(Transaction transaction) {
        if (transaction.getAmount() == null || transaction.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("O valor deve ser maior que zero");
        }

        transaction.setCreatedAt(LocalDateTime.now());
        transaction.setUpdatedAt(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);

        updateAccountBalance(saved);

        return saved;
    }

    private void updateAccountBalance(Transaction transaction) {
        Account account = accountService.getAccountById(transaction.getAccountId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        BigDecimal amount = transaction.getAmount();
        if (transaction.getType() == TransactionType.EXPENSE) {
            amount = amount.negate(); // subtrai
        }
        BigDecimal newBalance = account.getBalance().add(amount);
        accountService.updateBalance(account.getId(), newBalance);
    }

    private void revertAccountBalance(Transaction transaction) {
        Account account = accountService.getAccountById(transaction.getAccountId())
                .orElseThrow(() -> new RuntimeException("Conta não encontrada"));

        BigDecimal amount = transaction.getAmount();
        if (transaction.getType() == TransactionType.INCOME) {
            amount = amount.negate();
        }
        BigDecimal newBalance = account.getBalance().add(amount);
        accountService.updateBalance(account.getId(), newBalance);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public List<Transaction> getTransactionsByUser(String userId) {
        return transactionRepository.findByUserId(userId);
    }

    public List<Transaction> getTransactionsByAccount(String accountId) {
        return transactionRepository.findByAccountId(accountId);
    }

    public List<Transaction> getTransactionsByUserAndDateRange(String userId, LocalDateTime start, LocalDateTime end) {
        return transactionRepository.findByUserIdAndDateBetween(userId, start, end);
    }

    public Optional<Transaction> getTransactionById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        return transactionRepository.findById(id);
    }

    @Transactional
    public Transaction updateTransaction(String id, Transaction transactionDetails) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        Transaction existing = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));

        revertAccountBalance(existing);

        existing.setAccountId(transactionDetails.getAccountId());
        existing.setCategoryId(transactionDetails.getCategoryId());
        existing.setDescription(transactionDetails.getDescription());
        existing.setAmount(transactionDetails.getAmount());
        existing.setType(transactionDetails.getType());
        existing.setDate(transactionDetails.getDate());
        existing.setPaymentMethod(transactionDetails.getPaymentMethod());
        existing.setIsRecurring(transactionDetails.getIsRecurring());
        existing.setParentTransactionId(transactionDetails.getParentTransactionId());
        existing.setInstallmentNumber(transactionDetails.getInstallmentNumber());
        existing.setTotalInstallments(transactionDetails.getTotalInstallments());
        existing.setUpdatedAt(LocalDateTime.now());

        Transaction updated = transactionRepository.save(existing);

        updateAccountBalance(updated);

        return updated;
    }

    @Transactional
    public void deleteTransaction(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Transação não encontrada"));
        revertAccountBalance(transaction);
        transactionRepository.deleteById(id);
    }

    @Transactional
    public List<Transaction> createInstallments(Transaction parent, int totalInstallments) {
        parent.setInstallmentNumber(1);
        parent.setTotalInstallments(totalInstallments);
        parent.setParentTransactionId(null); // raiz
        Transaction savedParent = createTransaction(parent);

        for (int i = 2; i <= totalInstallments; i++) {
            Transaction installment = new Transaction();
            installment.setUserId(parent.getUserId());
            installment.setAccountId(parent.getAccountId());
            installment.setCategoryId(parent.getCategoryId());
            installment.setDescription(parent.getDescription() + " - Parcela " + i);
            installment.setAmount(parent.getAmount().divide(BigDecimal.valueOf(totalInstallments), 2, RoundingMode.HALF_EVEN));
            installment.setType(parent.getType());
            installment.setDate(parent.getDate().plusMonths(i - 1));
            installment.setPaymentMethod(parent.getPaymentMethod());
            installment.setIsRecurring(false);
            installment.setParentTransactionId(savedParent.getId());
            installment.setInstallmentNumber(i);
            installment.setTotalInstallments(totalInstallments);
            createTransaction(installment);
        }

        return transactionRepository.findByParentTransactionId(savedParent.getId());
    }

    public Page<Transaction> listTransactions(String userId, String accountId, String categoryId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) 
    {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10); 
        }
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        if (accountId != null) {
            query.addCriteria(Criteria.where("accountId").is(accountId));
        }
        if (categoryId != null) {
            query.addCriteria(Criteria.where("categoryId").is(categoryId));
        }
        if (type != null) {
            query.addCriteria(Criteria.where("type").is(type));
        }
        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("date").gte(startDate).lte(endDate));
        } else if (startDate != null) {
            query.addCriteria(Criteria.where("date").gte(startDate));
        } else if (endDate != null) {
            query.addCriteria(Criteria.where("date").lte(endDate));
        }

        long total = mongoTemplate.count(query, Transaction.class);
        query.with(pageable);
        List<Transaction> list = mongoTemplate.find(query, Transaction.class);
        return new PageImpl<>(list, pageable, total);
    }

    @SuppressWarnings("null")
    public Page<TransactionResponseDTO> listTransactionsWithDetails(String userId, String accountId, String categoryId, TransactionType type, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable) 
    {

        Page<Transaction> page = listTransactions(userId, accountId, categoryId, type, startDate, endDate, pageable);

        Set<String> accountIds = page.getContent().stream()
                .map(Transaction::getAccountId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Set<String> categoryIds = page.getContent().stream()
                .map(Transaction::getCategoryId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<String, String> accountNames = accountIds.isEmpty() 
            ? Collections.emptyMap() 
            : accountRepository.findAllById(accountIds)
                    .stream().collect(Collectors.toMap(Account::getId, Account::getName));

        Map<String, String> categoryNames = categoryIds.isEmpty() 
            ? Collections.emptyMap() 
            : categoryRepository.findAllById(categoryIds)
                    .stream().collect(Collectors.toMap(Category::getId, Category::getName));

        return page.map(transaction -> {
            TransactionResponseDTO dto = mapper.toTransactionResponseDTO(transaction);
            dto.setAccountName(accountNames.get(transaction.getAccountId()));
            if (transaction.getCategoryId() != null) {
                dto.setCategoryName(categoryNames.get(transaction.getCategoryId()));
            }
            return dto;
        });
    }

    
}