package com.example.maas.service;

import com.example.maas.entities.Role;
import com.example.maas.entities.Roles;
import com.example.maas.entities.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class JwtServiceTest {

    private JwtService jwtService;

    @BeforeEach
    void setUp() {
        jwtService = new JwtService();
    }

    @Test
    void generateToken_ShouldIncludeRoleClaim_WhenUserIsAdmin() {
        // Arrange
        Roles adminRole = new Roles(1L, Role.ADMIN);
        User user = User.builder()
                .username("admin")
                .password("password")
                .role(adminRole)
                .build();

        // Act
        String token = jwtService.generateToken(user);
        String role = (String) jwtService.extractClaim(token, claims -> claims.get("role"));

        // Assert
        assertNotNull(token);
        assertEquals("ADMIN", role);
    }

    @Test
    void generateToken_ShouldIncludeRoleClaim_WhenUserIsCustomer() {
        // Arrange
        Roles customerRole = new Roles(2L, Role.CUSTOMER);
        User user = User.builder()
                .username("customer")
                .password("password")
                .role(customerRole)
                .build();

        // Act
        String token = jwtService.generateToken(user);
        String role = (String) jwtService.extractClaim(token, claims -> claims.get("role"));

        // Assert
        assertNotNull(token);
        assertEquals("CUSTOMER", role);
    }
}
