package com.baudocapitao.api.model;

import com.baudocapitao.api.enums.TransactionType;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "categories")
public class Category {

    @Id
    private String id;
    private String userId;          
    private String name;
    private TransactionType type;   
    private String icon;            
    private Boolean isGlobal;       
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Category() {}

    public Category(String userId, String name, TransactionType type, String icon, Boolean isGlobal) {
        this.userId = userId;
        this.name = name;
        this.type = type;
        this.icon = icon;
        this.isGlobal = isGlobal;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public TransactionType getType() { return type; }
    public void setType(TransactionType type) { this.type = type; }

    public String getIcon() { return icon; }
    public void setIcon(String icon) { this.icon = icon; }

    public Boolean getIsGlobal() { return isGlobal; }
    public void setIsGlobal(Boolean isGlobal) { this.isGlobal = isGlobal; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}