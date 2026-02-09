package com.example.demo.users.application;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ChangePasswordCommand {

    private final String userId;
    private final String currentPassword;
    private final String newPassword;

}
