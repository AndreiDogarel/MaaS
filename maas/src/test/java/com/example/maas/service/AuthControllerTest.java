package com.example.maas.service;

import com.example.maas.controller.AuthController;
import com.example.maas.entities.Role;
import com.example.maas.entities.Roles;
import com.example.maas.entities.User;
import com.example.maas.entities.UserLoginDto;
import com.example.maas.entities.UserRegisterDto;
import com.example.maas.repository.RoleRepository;
import com.example.maas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private com.example.maas.service.JwtService jwtService;

    @InjectMocks
    private AuthController authController;

    @Test
    void login_success_returnsToken() {
        // Arrange
        User dbUser = User.builder()
                .username("jdoe")
                .password("$encoded$")
                .role(new Roles(2L, Role.CUSTOMER))
                .build();

        UserLoginDto login = new UserLoginDto();
        login.setUsername("jdoe");
        login.setPassword("rawpass");

        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("rawpass", dbUser.getPassword())).thenReturn(true);
        when(jwtService.generateToken(dbUser)).thenReturn("SOME.JWT.TOKEN");

        // Act
        ResponseEntity<?> response = authController.login(login);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("SOME.JWT.TOKEN", response.getBody());
    }

    @Test
    void login_invalidPassword_throwsRuntimeException() {
        // Arrange
        User dbUser = User.builder()
                .username("jdoe")
                .password("$encoded$")
                .role(new Roles(2L, Role.CUSTOMER))
                .build();

        UserLoginDto login = new UserLoginDto();
        login.setUsername("jdoe");
        login.setPassword("badpass");

        when(userRepository.findByUsername("jdoe")).thenReturn(Optional.of(dbUser));
        when(passwordEncoder.matches("badpass", dbUser.getPassword())).thenReturn(false);

        // Act & Assert: controller throws RuntimeException for invalid password
        assertThrows(RuntimeException.class, () -> authController.login(login));
    }

    @Test
    void register_success_encodesPasswordAndSavesUserWithRole() {
        // Arrange
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("alice");
        dto.setPassword("plain");
        dto.setRole("customer");

        when(passwordEncoder.encode("plain")).thenReturn("encoded-pass");
        Roles rolesEntity = new Roles(2L, Role.CUSTOMER);
        when(roleRepository.getByName(Role.CUSTOMER)).thenReturn(rolesEntity);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        String result = authController.register(dto);

        // Assert
        assertEquals("User registered successfully", result);
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(userRepository, times(1)).save(captor.capture());
        User saved = captor.getValue();
        assertEquals("alice", saved.getUsername());
        assertEquals("encoded-pass", saved.getPassword());
        assertNotNull(saved.getRole());
        assertEquals(Role.CUSTOMER, saved.getRole().getName());
    }

    @Test
    void register_throwsWhenRoleNotFound() {
        // Arrange
        UserRegisterDto dto = new UserRegisterDto();
        dto.setUsername("bob");
        dto.setPassword("p");
        dto.setRole("unknown");

        when(passwordEncoder.encode(anyString())).thenReturn("x");
        // roleRepository.getByName will be called with Role.valueOf("UNKNOWN") and we simulate it throwing an IllegalArgumentException
        // to mimic Role.valueOf throwing for invalid enum

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> authController.register(dto));
    }
}
