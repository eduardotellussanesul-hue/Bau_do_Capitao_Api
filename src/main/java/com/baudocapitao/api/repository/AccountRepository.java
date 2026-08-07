package com.baudocapitao.api.repository;

import com.baudocapitao.api.enums.AccountType;
import com.baudocapitao.api.model.Account;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface AccountRepository extends MongoRepository<Account, String> {
    List<Account> findByUserId(String userId); 
    Page<Account> findByUserId(String userId, Pageable pageable);
    Page<Account> findByUserIdAndType(String userId, AccountType type, Pageable pageable);
}
