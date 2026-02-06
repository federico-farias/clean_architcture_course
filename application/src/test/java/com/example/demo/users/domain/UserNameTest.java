package com.example.demo.users.domain;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserNameTest {

    @Test
    void shouldFailWhenUserNameIsEmpty() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new UserName("");
        });

        assertEquals("El nombre de usuario no puede ser nulo o vacío", exception.getMessage());
    }

        @Test
    void shouldFailWhenUserNameIsNull() {
        BusinessException exception = assertThrows(BusinessException.class, () -> {
            new UserName(null);
        });
        assertEquals("El nombre de usuario no puede ser nulo o vacío", exception.getMessage());
    }

        @Test
    void shouldCreateUserNameSuccessfully() {
        String validUserName = "validUser";
        UserName userName = new UserName(validUserName);
        assertEquals(validUserName, userName.getValue());
    }

}