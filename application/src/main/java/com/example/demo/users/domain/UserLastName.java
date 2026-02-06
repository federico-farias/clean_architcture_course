package com.example.demo.users.domain;

import lombok.Getter;

@Getter
public class UserLastName {

    private final String value;

    public UserLastName(String value) {
        if (value == null || value.trim().isEmpty()) {
            throw new BusinessException("El apellido no puede ser nulo o vacío");
        }
        this.value = value;
    }

}
