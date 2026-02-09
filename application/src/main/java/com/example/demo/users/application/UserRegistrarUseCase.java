package com.example.demo.users.application;

import com.example.demo.shared.DomainEventPublisher;
import com.example.demo.users.domain.*;

public class UserRegistrarUseCase {// Application
    
    private final UserRepository userRepository;
    private final DomainEventPublisher domainEventPublisher;

    public UserRegistrarUseCase(UserRepository userRepository, DomainEventPublisher domainEventPublisher) {
        this.userRepository = userRepository;
        this.domainEventPublisher = domainEventPublisher;
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

        user.changePassword("d@1wdEwdew", "x@1wdEwdew");

        // Guardar usuario
        userRepository.save(user);
        this.domainEventPublisher.publish(user.pullEvents());
        return convertToResponseDto(user);
    }
    
    private UserRegistrarResponse convertToResponseDto(User user) {
        return new UserRegistrarResponse(
                user.getId().getValue(),
                user.getUsername().getValue(),
                user.getEmail().getValue(),
                user.getFirstName().getValue(),
                user.getLastName().getValue(),
                user.getCreatedAt(),
                user.isEnabled()
        );
    }
}