package com.example.demo.users.domain;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class LombokTest {

    @Test
    public void myTest() {
        MyClass myObj = MyClass.builder().build();
        assertNotNull(myObj);
        System.out.println(myObj);
    }

    @Builder
    @Data
    @EqualsAndHashCode
    public static class MyClass {
        private String name;
        private String email;
        private int age;

        @Builder
        MyClass(String name, String email, int age) {
            this.name = name != null ? name : "Sam";
            this.email = email != null ? email : "sam@coppel.com";
            this.age = age != 0 ? age : 30;
        }
    }

}
