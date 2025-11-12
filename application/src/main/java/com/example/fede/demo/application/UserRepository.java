package com.example.fede.demo.application;

import java.util.List;
import java.util.Optional;

public interface UserRepository {
    
    Optional<User> findByUsername(String username);
    
    Optional<User> findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);
    
    List<User> findAllActiveUsers();
    
    List<User> findByName(String name);

    User save(User user);
}