package com.baudocapitao.api.dto;

import com.baudocapitao.api.enums.TransactionType;
import com.baudocapitao.api.enums.PaymentMethod;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TransactionResponseDTO {

    private String id;
    private String userId;
    private String accountId;
    private String accountName;       
    private String categoryId;
    private String categoryName;      
    private String description;
    private BigDecimal amount;
    private TransactionType type;
    private LocalDateTime date;
    private PaymentMethod paymentMethod;
    private Boolean isRecurring;
    private String parentTransactionId;
    private Integer installmentNumber;
    private Integer totalInstallments;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public TransactionResponseDTO() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

    public String getCategoryName() { return categoryName; }
    public void setCategoryName(String categoryName) { this.categoryName = categoryName; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public LocalDateTime getDate() { return date; }
    public void setDate(LocalDateTime date) { this.date = date; }

    public PaymentMethod getPaymentMethod() { return paymentMethod; }
    public void setPaymentMethod(PaymentMethod paymentMethod) { this.paymentMethod = paymentMethod; }

    public Boolean getIsRecurring() { return isRecurring; }
    public void setIsRecurring(Boolean isRecurring) { this.isRecurring = isRecurring; }

    public String getParentTransactionId() { return parentTransactionId; }
    public void setParentTransactionId(String parentTransactionId) { this.parentTransactionId = parentTransactionId; }

    public Integer getInstallmentNumber() { return installmentNumber; }
    public void setInstallmentNumber(Integer installmentNumber) { this.installmentNumber = installmentNumber; }

    public Integer getTotalInstallments() { return totalInstallments; }
    public void setTotalInstallments(Integer totalInstallments) { this.totalInstallments = totalInstallments; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}