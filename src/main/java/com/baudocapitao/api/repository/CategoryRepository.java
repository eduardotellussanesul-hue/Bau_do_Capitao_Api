package com.baudocapitao.api.repository;

import com.baudocapitao.api.model.Category;
import com.baudocapitao.api.enums.TransactionType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CategoryRepository extends MongoRepository<Category, String> {
    List<Category> findByUserIdOrIsGlobalTrue(String userId);
    List<Category> findByType(TransactionType type);
    List<Category> findByUserId(String userId);
    List<Category> findByIsGlobalTrue(); 
    Page<Category> findByIsGlobalTrue(Pageable pageable);
    Page<Category> findByIsGlobalTrueAndType(TransactionType type, Pageable pageable);
    Page<Category> findByUserIdOrIsGlobalTrue(String userId, Pageable pageable);
    Page<Category> findByUserIdOrIsGlobalTrueAndType(String userId, TransactionType type, Pageable pageable);
}