package com.example.demo.users.domain;

import lombok.Getter;

@Getter
public class UserName {

    private final String value;

    public UserName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException("El nombre de usuario no puede ser nulo o vacío");
        }
        this.value = value;
    }

}
