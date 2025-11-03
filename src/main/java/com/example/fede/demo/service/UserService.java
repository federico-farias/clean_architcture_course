package com.example.fede.demo.service;

import com.example.fede.demo.dto.UserRegistrationCommand;
import com.example.fede.demo.dto.UserRegistrationResponse;
import com.example.fede.demo.model.User;
import com.example.fede.demo.repository.UserRepository;

public class UserService {// Application
    
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserRegistrationResponse registerUser(UserRegistrationCommand registrationDto) {
        if (registrationDto.getUsername() == null || registrationDto.getUsername().isEmpty()) {
            throw new IllegalArgumentException("El nombre de usuario no puede ser nulo o vacío");
        }

        if (registrationDto.getEmail() == null || registrationDto.getEmail().isEmpty()) {
            throw new IllegalArgumentException("El email no puede ser nulo o vacío");
        }

        if (registrationDto.getFirstName() == null || registrationDto.getFirstName().isEmpty()) {
            throw new IllegalArgumentException("El nombre no puede ser nulo o vacío");
        }

        if (registrationDto.getLastName() == null || registrationDto.getLastName().isEmpty()) {
            throw new IllegalArgumentException("El apellido no puede ser nulo o vacío");
        }

        if (registrationDto.getPassword() == null || registrationDto.getPassword().isEmpty()) {
            throw new IllegalArgumentException("El password no puede ser nulo");
        }

        // Validar que las contraseñas coincidan
        if (!registrationDto.getPassword().equals(registrationDto.getConfirmPassword())) {
            throw new IllegalArgumentException("Las contraseñas no coinciden");
        }
        
        // Verificar que el username no exista
        if (userRepository.existsByUsername(registrationDto.getUsername())) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        
        // Verificar que el email no exista
        if (userRepository.existsByEmail(registrationDto.getEmail())) {
            throw new IllegalArgumentException("El email ya está registrado");
        }
        
        // Crear nuevo usuario
        User user = new User();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(registrationDto.getPassword());
        user.setFirstName(registrationDto.getFirstName());
        user.setLastName(registrationDto.getLastName());
        user.setEnabled(true);
        
        // Guardar usuario
        User savedUser = userRepository.save(user);
        
        // Convertir a DTO y retornar
        return convertToResponseDto(savedUser);
    }
    
    private UserRegistrationResponse convertToResponseDto(User user) {
        return new UserRegistrationResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.isEnabled()
        );
    }
}