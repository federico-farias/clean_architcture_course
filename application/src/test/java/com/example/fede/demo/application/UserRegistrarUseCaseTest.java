package com.example.fede.demo.application;

import com.example.demo.users.application.UserRegistrarCommand;
import com.example.demo.users.application.UserRegistrarResponse;
import com.example.demo.users.application.UserRegistrarUseCase;
import com.example.demo.users.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserRegistrarUseCaseTest {

    private UserRepository userRepository;

    private UserRegistrarUseCase userRegistrarUseCase;

    private UserRegistrarCommand validRegistrationDto;

    private User savedUser;

    @BeforeEach
    void setUp() {
        // Preparar datos de prueba
        validRegistrationDto = new UserRegistrarCommand(
                "testuser",
                "test@example.com",
                "password123",
                "password123",
                "John",
                "Doe"
        );

        // Simular el usuario guardado que retornaría el repositorio
        savedUser = new User(
                new UserId(UUID.randomUUID().toString()),
                new UserName("testuser"),
                new UserEmail("test@example.com"),
                new UserPassword("password123", "password123"),
                new UserFirstName("John"),
                new UserLastName("Doe"),
                LocalDateTime.now(),
                LocalDateTime.now(),
                true
        );
        // Simular que @PrePersist ya fue llamado
        savedUser.setCreatedAt(LocalDateTime.now());
    }

    @Test
    void registerUser_HappyPath_ShouldReturnResponseDto() {
        // Dado
        userRepository = new InMemoryStubUserRepository() {

            @Override
            public boolean existsByUsername(UserName username) {
                this.existsByUsernameCounter++;
                return !"testuser".equals(username.getValue());
            }

            @Override
            public boolean existsByEmail(UserEmail email) {
                this.existsByEmailCounter++;
                return !"test@example.com".equals(email.getValue());
            }

            @Override
            public User save(User user) {
                this.saveCounter++;
                user.setId(new UserId(UUID.randomUUID().toString()));
                user.setCreatedAt(LocalDateTime.now());
                return user;
            }

            public void saveVerify(int times) {
                assertEquals(times, this.saveCounter, "El método save debería ser llamado una vez");
            }

        };
        this.userRegistrarUseCase = new UserRegistrarUseCase(this.userRepository);

        // Cuando
        UserRegistrarResponse result = userRegistrarUseCase.register(validRegistrationDto);

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
    void registerUser_ShouldRaiseExceptionWhenNameIsNull() {
        // Dado
        userRepository = new InMemoryStubUserRepository() {};
        this.userRegistrarUseCase = new UserRegistrarUseCase(this.userRepository);

        validRegistrationDto.setUsername(null);

        BusinessException exception = assertThrows(
                BusinessException.class,
                () -> userRegistrarUseCase.register(validRegistrationDto)
        );
        assertEquals("El nombre de usuario no puede ser nulo o vacío", exception.getMessage());
    }

}