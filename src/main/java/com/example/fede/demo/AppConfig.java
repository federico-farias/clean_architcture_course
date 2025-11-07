package com.example.fede.demo;

import com.example.fede.demo.portout.UserRepositoryDelegate;
import com.example.fede.demo.portout.UserRepositorySpringData;
import com.example.fede.demo.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserService userService(UserRepositorySpringData repositorySpringData) {
        return new UserService(new UserRepositoryDelegate(repositorySpringData));
    }

}
