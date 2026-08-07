package com.baudocapitao.api.mapper;

import com.baudocapitao.api.dto.*;
import com.baudocapitao.api.model.*;
import org.springframework.stereotype.Component;

@Component
public class Mapper {

    public UserResponseDTO toUserResponseDTO(User user) {
        if (user == null) return null;
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setCreatedAt(user.getCreatedAt());
        dto.setUpdatedAt(user.getUpdatedAt());
        return dto;
    }

    public AccountResponseDTO toAccountResponseDTO(Account account) {
        if (account == null) return null;
        AccountResponseDTO dto = new AccountResponseDTO();
        dto.setId(account.getId());
        dto.setUserId(account.getUserId());
        dto.setName(account.getName());
        dto.setType(account.getType());
        dto.setBalance(account.getBalance());
        dto.setCreatedAt(account.getCreatedAt());
        dto.setUpdatedAt(account.getUpdatedAt());
        return dto;
    }

    public CategoryResponseDTO toCategoryResponseDTO(Category category) {
        if (category == null) return null;
        CategoryResponseDTO dto = new CategoryResponseDTO();
        dto.setId(category.getId());
        dto.setUserId(category.getUserId());
        dto.setName(category.getName());
        dto.setType(category.getType());
        dto.setIcon(category.getIcon());
        dto.setIsGlobal(category.getIsGlobal());
        dto.setCreatedAt(category.getCreatedAt());
        dto.setUpdatedAt(category.getUpdatedAt());
        return dto;
    }

    public TransactionResponseDTO toTransactionResponseDTO(Transaction transaction) {
        if (transaction == null) return null;
        TransactionResponseDTO dto = new TransactionResponseDTO();
        dto.setId(transaction.getId());
        dto.setUserId(transaction.getUserId());
        dto.setAccountId(transaction.getAccountId());
        dto.setCategoryId(transaction.getCategoryId());
        dto.setDescription(transaction.getDescription());
        dto.setAmount(transaction.getAmount());
        dto.setType(transaction.getType());
        dto.setDate(transaction.getDate());
        dto.setPaymentMethod(transaction.getPaymentMethod());
        dto.setIsRecurring(transaction.getIsRecurring());
        dto.setParentTransactionId(transaction.getParentTransactionId());
        dto.setInstallmentNumber(transaction.getInstallmentNumber());
        dto.setTotalInstallments(transaction.getTotalInstallments());
        dto.setCreatedAt(transaction.getCreatedAt());
        dto.setUpdatedAt(transaction.getUpdatedAt());
        return dto;
    }
}