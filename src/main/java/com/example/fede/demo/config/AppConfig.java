package com.example.fede.demo.config;

import com.example.fede.demo.repository.UserRepositoryDelegate;
import com.example.fede.demo.repository.UserRepositorySpringData;
import com.example.fede.demo.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserService userService(UserRepositorySpringData repositorySpringData) {
        return new UserService(new UserRepositoryDelegate(repositorySpringData));
    }

}
