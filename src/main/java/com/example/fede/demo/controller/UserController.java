package com.example.fede.demo.controller;

import com.example.fede.demo.dto.UserRegistrationCommand;
import com.example.fede.demo.dto.UserRegistrationDto;
import com.example.fede.demo.dto.UserRegistrationResponse;
import com.example.fede.demo.dto.UserResponseDto;
import com.example.fede.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {
    
    private final UserService userService;
    
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRegistrationDto registrationDto) {
        try {
            UserRegistrationCommand command = new UserRegistrationCommand(
                    registrationDto.getUsername(),
                    registrationDto.getEmail(),
                    registrationDto.getPassword(),
                    registrationDto.getConfirmPassword(),
                    registrationDto.getFirstName(),
                    registrationDto.getLastName()
            );
            UserRegistrationResponse resultCommand = userService.registerUser(command);
            UserResponseDto userResponse = new UserResponseDto(
                    resultCommand.getId(),
                    resultCommand.getUsername(),
                    resultCommand.getEmail(),
                    resultCommand.getFirstName(),
                    resultCommand.getLastName(),
                    resultCommand.getCreatedAt(),
                    resultCommand.isEnabled()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(userResponse);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new ErrorResponse(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Error interno del servidor"));
        }
    }
    
    // Clase interna para respuestas de error
    public static class ErrorResponse {
        private String message;
        
        public ErrorResponse(String message) {
            this.message = message;
        }
        
        public String getMessage() {
            return message;
        }
        
        public void setMessage(String message) {
            this.message = message;
        }
    }
}