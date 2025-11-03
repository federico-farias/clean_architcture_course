package com.example.fede.demo;

import com.example.fede.demo.dto.UserRegistrationDto;
import com.example.fede.demo.dto.UserResponseDto;
import com.example.fede.demo.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class DemoApplicationTests {

    @Autowired
    private UserService userService;

    @Test
    void contextLoads() {
    }

    @Test
    @Transactional
    void testUserRegistration() {
        // Crear DTO de registro
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setConfirmPassword("password123");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("User");

        // Registrar usuario
        UserResponseDto response = userService.registerUser(registrationDto);

        // Verificar que el usuario se registró correctamente
        assertNotNull(response);
        assertNotNull(response.getId());
        assertEquals("testuser", response.getUsername());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test", response.getFirstName());
        assertEquals("User", response.getLastName());
        assertTrue(response.isEnabled());
        assertNotNull(response.getCreatedAt());
    }

    @Test
    @Transactional
    void testUserRegistrationWithMismatchedPasswords() {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("test@example.com");
        registrationDto.setPassword("password123");
        registrationDto.setConfirmPassword("differentpassword");
        registrationDto.setFirstName("Test");
        registrationDto.setLastName("User");

        // Verificar que se lanza excepción cuando las contraseñas no coinciden
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(registrationDto);
        });
    }

    @Test
    @Transactional
    void testDuplicateUsernameRegistration() {
        // Registrar primer usuario
        UserRegistrationDto firstUser = new UserRegistrationDto();
        firstUser.setUsername("testuser");
        firstUser.setEmail("test1@example.com");
        firstUser.setPassword("password123");
        firstUser.setConfirmPassword("password123");
        firstUser.setFirstName("Test");
        firstUser.setLastName("User");
        
        userService.registerUser(firstUser);

        // Intentar registrar usuario con mismo username
        UserRegistrationDto secondUser = new UserRegistrationDto();
        secondUser.setUsername("testuser"); // Mismo username
        secondUser.setEmail("test2@example.com");
        secondUser.setPassword("password123");
        secondUser.setConfirmPassword("password123");
        secondUser.setFirstName("Test");
        secondUser.setLastName("User2");

        // Verificar que se lanza excepción por username duplicado
        assertThrows(IllegalArgumentException.class, () -> {
            userService.registerUser(secondUser);
        });
    }

    @Test
    @Transactional
    void testGetAllUsers() {
        // Registrar algunos usuarios
        registerTestUser("user1", "user1@test.com", "Test1", "User1");
        registerTestUser("user2", "user2@test.com", "Test2", "User2");

        // Obtener todos los usuarios
        List<UserResponseDto> users = userService.getAllUsers();

        // Verificar que se obtuvieron los usuarios
        assertNotNull(users);
        assertEquals(2, users.size());
    }

    private void registerTestUser(String username, String email, String firstName, String lastName) {
        UserRegistrationDto registrationDto = new UserRegistrationDto();
        registrationDto.setUsername(username);
        registrationDto.setEmail(email);
        registrationDto.setPassword("password123");
        registrationDto.setConfirmPassword("password123");
        registrationDto.setFirstName(firstName);
        registrationDto.setLastName(lastName);
        
        userService.registerUser(registrationDto);
    }
}
