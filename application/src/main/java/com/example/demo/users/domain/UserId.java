package com.example.demo.users.domain;

import lombok.Getter;

import java.util.UUID;

@Getter
public class UserId {

    private final String id;

    public UserId(String id) {
        if  (id == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }
        this.id = id;
    }

    public UserId(){
        this.id = UUID.randomUUID().toString();
    }

}
