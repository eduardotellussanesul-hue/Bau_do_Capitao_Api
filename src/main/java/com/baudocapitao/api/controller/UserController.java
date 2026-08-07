package com.baudocapitao.api.controller;

import com.baudocapitao.api.dto.UserResponseDTO;
import com.baudocapitao.api.mapper.Mapper;
import com.baudocapitao.api.model.User;
import com.baudocapitao.api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.data.domain.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "CRUD de usuários")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private Mapper mapper;

    @PostMapping
    @Operation(summary = "Cria um novo usuário")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        User saved = userService.createUser(user);
        return new ResponseEntity<>(saved, HttpStatus.CREATED);
    }

    @GetMapping("/pagination")
    public Page<UserResponseDTO> listUsers(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            Pageable pageable) {
        return userService.listUsers(name, email, pageable)
                .map(mapper::toUserResponseDTO);
    }

    @GetMapping
    @Operation(summary = "Lista todos os usuários")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Busca um usuário pelo ID")
    public ResponseEntity<User> getUserById(@PathVariable String id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Busca um usuário pelo e-mail")
    public ResponseEntity<User> getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualiza um usuário existente")
    public ResponseEntity<User> updateUser(@PathVariable String id, @RequestBody User user) {
        try {
            User updated = userService.updateUser(id, user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deleta um usuário pelo ID")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    
}