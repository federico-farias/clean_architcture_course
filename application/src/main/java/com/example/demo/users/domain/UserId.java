package com.example.demo.users.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserId {

    private final String value;

    public UserId(String value) {
        if  (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        this.value = value;
    }

    public UserId(){
        this.value = UUID.randomUUID().toString();
    }

}
