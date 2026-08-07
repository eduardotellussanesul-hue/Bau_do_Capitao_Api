package com.baudocapitao.api.service;

import com.baudocapitao.api.model.Category;
import com.baudocapitao.api.repository.CategoryRepository;
import com.baudocapitao.api.enums.TransactionType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    public Category createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public List<Category> getAllCategoriesForUser(String userId) {
        return categoryRepository.findByUserIdOrIsGlobalTrue(userId);
    }

    public List<Category> getCategoriesByUserId(String userId) {
        return categoryRepository.findByUserId(userId);
    }

    public List<Category> getGlobalCategories() {
        return categoryRepository.findByIsGlobalTrue();
    }

    public List<Category> getCategoriesByType(TransactionType type) {
        return categoryRepository.findByType(type);
    }

    public Optional<Category> getCategoryById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        return categoryRepository.findById(id);
    }

    public Category updateCategory(String id, Category categoryDetails) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoria não encontrada com id: " + id));
        category.setName(categoryDetails.getName());
        category.setType(categoryDetails.getType());
        category.setIcon(categoryDetails.getIcon());
        category.setUpdatedAt(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public void deleteCategory(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        categoryRepository.deleteById(id);
    }

    public void initDefaultGlobalCategories() {
        if (categoryRepository.findByIsGlobalTrue().isEmpty()) {
            Category[] defaults = {
                new Category(null, "Salário", TransactionType.INCOME, "💰", true),
                new Category(null, "Freelance", TransactionType.INCOME, "💻", true),
                new Category(null, "Investimentos", TransactionType.INCOME, "📈", true),
                new Category(null, "Alimentação", TransactionType.EXPENSE, "🍔", true),
                new Category(null, "Moradia", TransactionType.EXPENSE, "🏠", true),
                new Category(null, "Transporte", TransactionType.EXPENSE, "🚗", true),
                new Category(null, "Saúde", TransactionType.EXPENSE, "🏥", true),
                new Category(null, "Educação", TransactionType.EXPENSE, "📚", true),
                new Category(null, "Lazer", TransactionType.EXPENSE, "🎮", true),
                new Category(null, "Vestuário", TransactionType.EXPENSE, "👕", true),
                new Category(null, "Assinaturas", TransactionType.EXPENSE, "📺", true),
                new Category(null, "Outros", TransactionType.EXPENSE, "📦", true)
            };
            for (Category cat : defaults) {
                createCategory(cat);
            }
        }
    }
    public Page<Category> listCategories(String userId, TransactionType type, Pageable pageable) {
        if (userId != null && type != null) {
            return categoryRepository.findByUserIdOrIsGlobalTrueAndType(userId, type, pageable);
        } else if (userId != null) {
            return categoryRepository.findByUserIdOrIsGlobalTrue(userId, pageable);
        } else if (type != null) {
            return categoryRepository.findByIsGlobalTrueAndType(type, pageable);
        }
        return categoryRepository.findByIsGlobalTrue(pageable);
    }
}