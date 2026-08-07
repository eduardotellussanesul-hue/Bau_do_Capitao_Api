package com.baudocapitao.api.controller;

import com.baudocapitao.api.model.Category;
import com.baudocapitao.api.service.CategoryService;
import com.baudocapitao.api.dto.CategoryResponseDTO;
import com.baudocapitao.api.enums.TransactionType;
import com.baudocapitao.api.mapper.Mapper;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Categories", description = "CRUD de categorias de transações")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    @Operation(summary = "Cria uma nova categoria (pessoal ou global)")
    public ResponseEntity<Category> createCategory(@RequestBody Category category) {
        Category saved = categoryService.createCategory(category);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Lista todas as categorias (globais + pessoais de um usuário)")
    public List<Category> getAllCategories(@RequestParam String userId) {
        return categoryService.getAllCategoriesForUser(userId);
    }

    @GetMapping("/global")
    @Operation(summary = "Lista apenas categorias globais")
    public List<Category> getGlobalCategories() {
        return categoryService.getGlobalCategories();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Busca categorias pessoais por ID do usuário")
    public List<Category> getCategoriesByUser(@PathVariable String userId) {
        return categoryService.getCategoriesByUserId(userId);
    }

    @GetMapping("/type/{type}")
    @Operation(summary = "Busca categorias por tipo (INCOME ou EXPENSE)")
    public List<Category> getCategoriesByType(@PathVariable TransactionType type) {
        return categoryService.getCategoriesByType(type);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca uma categoria pelo ID")
    public ResponseEntity<Category> getCategoryById(@PathVariable String id) {
        return categoryService.getCategoryById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza uma categoria existente")
    public ResponseEntity<Category> updateCategory(@PathVariable String id, @RequestBody Category category) {
        try {
            Category updated = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta uma categoria pelo ID")
    public ResponseEntity<Void> deleteCategory(@PathVariable String id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
    
    @GetMapping("/pagination")
    @Operation(summary = "Lista categorias com paginação e filtros (globais + pessoais)")
    public Page<CategoryResponseDTO> listCategories(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) TransactionType type,
            Pageable pageable) {
        return categoryService.listCategories(userId, type, pageable)
                .map(mapper::toCategoryResponseDTO);
    }
}