package com.baudocapitao.api.repository;

import com.baudocapitao.api.model.Transaction;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByUserId(String userId);
    List<Transaction> findByAccountId(String accountId);
    List<Transaction> findByUserIdAndDateBetween(String userId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByUserIdAndType(String userId, String type);
    List<Transaction> findByAccountIdAndDateBetween(String accountId, LocalDateTime start, LocalDateTime end);
    List<Transaction> findByParentTransactionId(String parentTransactionId);
}