package com.example.fede.demo.repository;

import com.example.fede.demo.model.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findAllActiveUsers();
    
    List<User> findByName(String name);

    User save(User user);
}