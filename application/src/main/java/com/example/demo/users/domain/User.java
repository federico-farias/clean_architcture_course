package com.example.demo.users.domain;

import com.example.demo.shared.RootAggregate;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class User extends RootAggregate {
    
    private UserId id;
    
    private UserName username;
    
    private UserEmail email;
    
    private UserPassword password;
    
    private UserFirstName firstName;
    
    private UserLastName lastName;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;

    private boolean active = false;
    
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
            boolean active,
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
        this.active = active;
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
                false,
                true
        );
        UserCreatedEvent event = new UserCreatedEvent(
                user.getId().getValue(),
                user.getUsername().getValue(),
                user.getEmail().getValue()
        );
        user.record(event);
        return user;
    }

    public void changePassword(String oldPassword, String newPassword) {
        if (!this.password.getValue().equals(oldPassword)) {
            throw new BusinessException("Old password does not match.");
        }
        this.password = new UserPassword(newPassword);
        this.updatedAt = LocalDateTime.now();
        UserPasswordChangedEvent event = new UserPasswordChangedEvent(
                this.id.getValue(),
                this.username.getValue()
        );
        this.record(event);
    }

}