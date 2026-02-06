package com.example.demo.users.application;

import com.example.demo.users.domain.*;

public class UserRegistrarUseCase {// Application
    
    private final UserRepository userRepository;

    public UserRegistrarUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserRegistrarResponse register(UserRegistrarCommand registrationDto) {
        UserName userName = new UserName(registrationDto.getUsername());
        UserEmail userEmail = new UserEmail(registrationDto.getEmail());
        UserFirstName userFirstName = new UserFirstName(registrationDto.getFirstName());
        UserLastName userLastName = new UserLastName(registrationDto.getLastName());
        UserPassword userPassword = new UserPassword(registrationDto.getPassword(), registrationDto.getConfirmPassword());

        User user = User.create(userName, userEmail, userPassword, userFirstName, userLastName);

        if (userRepository.existsByUsername(userName)) {
            throw new IllegalArgumentException("El nombre de usuario ya existe");
        }
        if (userRepository.existsByEmail(userEmail)) {
            throw new IllegalArgumentException("El email ya está registrado");
        }

        // Guardar usuario
        User savedUser = userRepository.save(user);
        
        // Convertir a DTO y retornar
        return convertToResponseDto(savedUser);
    }
    
    private UserRegistrarResponse convertToResponseDto(User user) {
        /*
        return new UserRegistrarResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getCreatedAt(),
                user.isEnabled()
        );
         */
        return null;
    }
}