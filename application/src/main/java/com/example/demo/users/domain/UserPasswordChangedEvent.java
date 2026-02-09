package com.example.demo.users.domain;

import com.example.demo.shared.DomainEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPasswordChangedEvent extends DomainEvent {

    private final String userId;
    private final String email;

}
