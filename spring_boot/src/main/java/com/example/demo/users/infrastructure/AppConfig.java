package com.example.demo.users.infrastructure;

import com.example.demo.users.infrastructure.portout.UserRepositoryDelegate;
import com.example.demo.users.infrastructure.portout.UserRepositorySpringData;
import com.example.demo.users.application.UserRegistrarUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserRegistrarUseCase userService(UserRepositorySpringData repositorySpringData) {
        return new UserRegistrarUseCase(new UserRepositoryDelegate(repositorySpringData));
    }

}
