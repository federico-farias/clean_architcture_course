package com.example.fede.demo.application;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    
    private Long id;
    
    private String username;
    
    private String email;
    
    private String password;
    
    private String firstName;
    
    private String lastName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean enabled = true;
}