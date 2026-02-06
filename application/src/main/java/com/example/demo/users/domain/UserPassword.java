package com.example.demo.users.domain;

import lombok.Getter;

@Getter
public class UserPassword {

    private final String password;
    private final String confirmPassword;

    public UserPassword(String password, String confirmPassword) {
        if (password == null || password.isEmpty()) {
            throw new BusinessException("El password no puede ser nulo");
        }
        if (!password.equals(confirmPassword)) {
            throw new BusinessException("Las contraseñas no coinciden");
        }
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

}
