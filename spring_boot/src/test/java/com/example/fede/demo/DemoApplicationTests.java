package com.example.fede.demo;

import com.example.demo.users.application.UserRegistrarUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DemoApplicationTests {

    @Autowired
    private UserRegistrarUseCase userRegistrarUseCase;

    @Test
    void contextLoads() {
    }

}
