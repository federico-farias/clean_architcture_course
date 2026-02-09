package com.example.demo.users.domain;

import lombok.Getter;

public class UserPassword {

    private final String password;
    private final String confirmPassword;

    public UserPassword(String password, String confirmPassword) {
        if (password == null || password.isEmpty()) {
            throw new BusinessException("El password no puede ser nulo");
        }
        if (password.length() < 6) {
            throw new BusinessException("El password no puede ser menor de 6 caracteres");
        }
        if (!this.containNumbers(password)) {
            throw new BusinessException("El password debe contener al menos un número");
        }
        if (!this.containUpperCase(password)) {
            throw new BusinessException("El password debe contener al menos una letra mayúscula");
        }
        if (!this.containSpecialChar(password)) {
            throw new BusinessException("El password debe contener al menos un carácter especial");
        }
        if (!password.equals(confirmPassword)) {
            throw new BusinessException("Las contraseñas no coinciden");
        }
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    private boolean containSpecialChar(String password) {
        return password.matches(".*[!@#$%^&*(),.?\":{}|<>].*");
    }

    private boolean containUpperCase(String password) {
        return password.matches(".*[A-Z].*");
    }

    private boolean containNumbers(String password) {
        return password.matches(".*\\d.*");
    }

    public UserPassword(String password) {
        if (password == null || password.isEmpty()) {
            throw new BusinessException("El password no puede ser nulo");
        }
        this.password = password;
        this.confirmPassword = password;
    }

    public String getValue() {
        return this.password;
    }

}
