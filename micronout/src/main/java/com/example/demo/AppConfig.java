package com.example.demo;

import com.example.demo.users.application.UserRegistrarUseCase;
import com.example.demo.users.infrastructure.portout.MemoryUserRepository;
import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class AppConfig {

    @Singleton
    public UserRegistrarUseCase userService(MemoryUserRepository repositoryInMemory) {
        return new UserRegistrarUseCase(repositoryInMemory);
    }

}
