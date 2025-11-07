package com.example.fede.demo.service;

import com.example.fede.demo.application.UserService;
import com.example.fede.demo.portin.UserRegistrationDto;
import com.example.fede.demo.portin.UserResponseDto;
import com.example.fede.demo.application.User;
import com.example.fede.demo.application.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRegistrationDto validRegistrationDto;
    private User savedUser;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        validRegistrationDto = new UserRegistrationDto(
                "testuser",
                "test@example.com",
                "password123",
                "password123",
                "John",
                "Doe"
        );

        // Simular el usuario guardado que retornaría el repositorio
        savedUser = new User();
        savedUser.setId(1L);
        savedUser.setUsername("testuser");
        savedUser.setEmail("test@example.com");
        savedUser.setPassword("password123");
        savedUser.setFirstName("John");
        savedUser.setLastName("Doe");
        savedUser.setEnabled(true);
        // Simular que @PrePersist ya fue llamado
        savedUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void registerUser_HappyPath_ShouldReturnUserResponseDto() {
        // Dado
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Cuando
        UserResponseDto result = userService.registerUser(validRegistrationDto);

        // Entonces
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertTrue(result.isEnabled());
        assertNotNull(result.getCreatedAt());

        // Verify: Verificar que se llamaron los métodos esperados
        verify(userRepository, times(1)).existsByUsername("testuser");
        verify(userRepository, times(1)).existsByEmail("test@example.com");
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void registerUser_ShouldRaiseExceptionWhenUserNameIsNull() {
        // Dado
        validRegistrationDto.setUsername(null);

        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.registerUser(validRegistrationDto)
        );
        assertEquals("El nombre de usuario no puede ser nulo o vacío", exception.getMessage());
    }

}