package com.baudocapitao.api.service;

import com.baudocapitao.api.model.User;
import com.baudocapitao.api.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User createUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public Page<User> listUsers(String name, String email, Pageable pageable) {
        if (pageable == null) {
            pageable = PageRequest.of(0, 10); 
        }
        if (name != null && email != null) {
            return userRepository.findByNameContainingIgnoreCaseAndEmailContainingIgnoreCase(name, email, pageable);
        } else if (name != null) {
            return userRepository.findByNameContainingIgnoreCase(name, pageable);
        } else if (email != null) {
            return userRepository.findByEmailContainingIgnoreCase(email, pageable);
        } else {
            return userRepository.findAll(pageable);
        }
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User updateUser(String id, User userDetails) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado com id: " + id));
        user.setName(userDetails.getName());
        user.setEmail(userDetails.getEmail());
        user.setPasswordHash(userDetails.getPasswordHash());
        user.setUpdatedAt(LocalDateTime.now());
        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        if (id == null) {
            throw new IllegalArgumentException("ID não podem ser nulo");
        }
        userRepository.deleteById(id);
    }

    public boolean login(String email, String passwordHash) 
    {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            return user.getPasswordHash().equals(passwordHash);
        }
        return false;
    }
}