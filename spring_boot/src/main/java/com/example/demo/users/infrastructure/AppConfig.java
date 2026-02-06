package com.example.demo.users.infrastructure;

import com.example.demo.users.infrastructure.portout.UserRepositoryDelegate;
import com.example.demo.users.infrastructure.portout.UserRepositorySpringData;
import com.example.demo.users.application.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public UserService userService(UserRepositorySpringData repositorySpringData) {
        return new UserService(new UserRepositoryDelegate(repositorySpringData));
    }

}
