package com.example.demo.users.domain;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class User {
    
    private UserId id;
    
    private UserName username;
    
    private UserEmail email;
    
    private UserPassword password;
    
    private UserFirstName firstName;
    
    private UserLastName lastName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean enabled = true;

    public User(
            UserId id,
            UserName username,
            UserEmail email,
            UserPassword password,
            UserFirstName firstName,
            UserLastName lastName,
            LocalDateTime createdAt,
            LocalDateTime updatedAt,
            boolean enabled
    ) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.enabled = enabled;
    }

    public static User create(
            UserName userName,
            UserEmail userEmail,
            UserPassword userPassword,
            UserFirstName userFirstName,
            UserLastName userLastName
    ) {
        User user = new User(
                new UserId(),
                userName,
                userEmail,
                userPassword,
                userFirstName,
                userLastName,
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        return user;
    }

}