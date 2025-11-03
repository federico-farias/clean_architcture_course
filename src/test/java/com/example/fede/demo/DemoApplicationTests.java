package com.example.fede.demo;

import com.example.fede.demo.dto.UserRegistrationDto;
import com.example.fede.demo.dto.UserResponseDto;
import com.example.fede.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

}
