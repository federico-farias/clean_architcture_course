package com.example.demo;

import com.example.demo.users.application.UserService;
import com.example.demo.users.infrastructure.portout.MemoryUserRepository;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class AppConfig {

    @Singleton
    public UserService userService(MemoryUserRepository repositoryInMemory) {
        return new UserService(repositoryInMemory);
    }

}
