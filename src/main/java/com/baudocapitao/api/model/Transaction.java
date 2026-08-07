package com.baudocapitao.api.model;

import com.baudocapitao.api.enums.PaymentMethod;
import com.baudocapitao.api.enums.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Document(collection = "transactions")
public class Transaction {

    @Id
    private String id;
    private String userId;
    private String accountId;
    private String categoryId;       
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

    public Transaction() {
    }

    public Transaction(String userId, String accountId, String categoryId, String description,
                       BigDecimal amount, TransactionType type, LocalDateTime date,
                       PaymentMethod paymentMethod, Boolean isRecurring,
                       String parentTransactionId, Integer installmentNumber, Integer totalInstallments) {
        this.userId = userId;
        this.accountId = accountId;
        this.categoryId = categoryId;
        this.description = description;
        this.amount = amount;
        this.type = type;
        this.date = date;
        this.paymentMethod = paymentMethod;
        this.isRecurring = isRecurring != null ? isRecurring : false;
        this.parentTransactionId = parentTransactionId;
        this.installmentNumber = installmentNumber;
        this.totalInstallments = totalInstallments;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getAccountId() { return accountId; }
    public void setAccountId(String accountId) { this.accountId = accountId; }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = categoryId; }

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