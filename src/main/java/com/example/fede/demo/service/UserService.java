package com.example.fede.demo.service;

import com.example.fede.demo.dto.UserRegistrationDto;
import com.example.fede.demo.dto.UserResponseDto;
import com.example.fede.demo.model.User;
import com.example.fede.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    
    public UserResponseDto registerUser(UserRegistrationDto registrationDto) {
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
    
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public List<UserResponseDto> getAllActiveUsers() {
        return userRepository.findAllActiveUsers()
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserById(Long id) {
        return userRepository.findById(id)
                .map(this::convertToResponseDto);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(this::convertToResponseDto);
    }
    
    @Transactional(readOnly = true)
    public Optional<UserResponseDto> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToResponseDto);
    }
    
    @Transactional(readOnly = true)
    public List<UserResponseDto> searchUsersByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());
    }
    
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("Usuario no encontrado");
        }
        userRepository.deleteById(id);
    }
    
    public UserResponseDto disableUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));
        
        user.setEnabled(false);
        User updatedUser = userRepository.save(user);
        return convertToResponseDto(updatedUser);
    }
    
    private UserResponseDto convertToResponseDto(User user) {
        return new UserResponseDto(
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