package com.example.demo.users.domain;

import com.example.demo.shared.DomainEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode(callSuper = true)
public class UserCreatedEvent extends DomainEvent {

    private final String userId;
    private final String username;
    private final String email;

    public UserCreatedEvent(String userId, String username, String email) {
        this.userId = userId;
        this.username = username;
        this.email = email;
    }

}
