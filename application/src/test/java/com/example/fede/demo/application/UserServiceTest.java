package com.example.fede.demo.application;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    private UserRepository userRepository;

    private UserService userService;

    private UserRegistrationCommand validRegistrationDto;

    private User savedUser;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        validRegistrationDto = new UserRegistrationCommand(
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
        userRepository = new InMemoryStubUserRepository() {

            @Override
            public boolean existsByUsername(String username) {
                this.existsByUsernameCounter++;
                return !"testuser".equals(username);
            }

            @Override
            public boolean existsByEmail(String email) {
                this.existsByEmailCounter++;
                return !"test@example.com".equals(email);
            }

            @Override
            public User save(User user) {
                this.saveCounter++;
                user.setId(1L);
                user.setCreatedAt(LocalDateTime.now());
                return user;
            }

            public void saveVerify(int times) {
                assertEquals(times, this.saveCounter, "El método save debería ser llamado una vez");
            }

        };
        this.userService = new UserService(this.userRepository);

        // Cuando
        UserRegistrationResponse result = userService.registerUser(validRegistrationDto);

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
        InMemoryStubUserRepository verifier = (InMemoryStubUserRepository) userRepository;
        verifier.existsByUsernameVerifier(1);
        verifier.existsByEmailVerifier(1);
        verifier.saveVerify(1);
    }

    @Test
    void registerUser_ShouldRaiseExceptionWhenUserNameIsNull() {
        // Dado
        userRepository = new InMemoryStubUserRepository() {};
        this.userService = new UserService(this.userRepository);

        validRegistrationDto.setUsername(null);

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> userService.registerUser(validRegistrationDto)
        );
        assertEquals("El nombre de usuario no puede ser nulo o vacío", exception.getMessage());
    }

}