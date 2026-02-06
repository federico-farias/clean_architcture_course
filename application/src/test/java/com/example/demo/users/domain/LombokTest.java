package com.example.demo.users.domain;

import lombok.Builder;
import lombok.Data;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LombokTest {

    @Test
    public void myTest() {
        MyClass myObj = MyClass.builder().build();
        assertNotNull(myObj);
    }

    @Builder
    @Data
    public static class MyClass {
        private String name;
        private String email;
        private int age;
    }

}
