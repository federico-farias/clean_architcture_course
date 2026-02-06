package com.example.demo.users.domain;

import lombok.Getter;

@Getter
public class UserEmail {

    private final String value;

    public UserEmail(String value) {
        if (value == null || value.isEmpty()) {
            throw new BusinessException("El email no puede ser nulo o vacío");
        }
        this.value = value;
    }

}
