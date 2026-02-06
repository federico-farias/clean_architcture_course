package com.example.demo.users.domain;

import lombok.Getter;

@Getter
public class UserFirstName {

    private final String value;

    public UserFirstName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException("El nombre no puede ser nulo o vacío");
        }
        this.value = value;
    }

}
