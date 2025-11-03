package com.example.fede.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRegistrationDto {

    private String username;

    private String email;

    private String password;

    private String confirmPassword;

    private String firstName;

    private String lastName;

}