package com.example.maas.controller;

import com.example.maas.entities.*;
import com.example.maas.repository.RoleRepository;
import com.example.maas.repository.UserRepository;
import com.example.maas.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public String register(@RequestBody UserRegisterDto user) {
        System.out.println(user.toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User user1 = new User(user);
        user1.setRole(roleRepository.getByName(Role.valueOf(user.getRole().toString().toUpperCase())));
        userRepository.save(user1);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto user) {
        System.out.println(">>> Raw password received: '" + user.getPassword() + "'");
        System.out.println(">>> Username received: '" + user.getUsername() + "'");

        var existing = userRepository.findByUsername(user.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        System.out.println(">>> Encoded password from DB: '" + existing.getPassword() + "'");
        System.out.println(">>> Encoder type: " + passwordEncoder.getClass());
        System.out.println(">>> Matches? " + passwordEncoder.matches(user.getPassword(), existing.getPassword()));

        if (!passwordEncoder.matches(user.getPassword(), existing.getPassword())) {
            throw new RuntimeException("Invalid password");
        }

        String token = jwtService.generateToken(existing);
        return ResponseEntity.ok(token);
    }

    @GetMapping("/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String test() {
        return "test";
    }

    @GetMapping("/validate")
    public ResponseEntity<UserDto> validate() {
        System.out.println(">>> Validate user");
        Authentication authentication = SecurityContextHolder
                .getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            User user = (User) authentication.getPrincipal();
            System.out.println(">>> User: " + user.getUsername());
            return ResponseEntity.ok(new UserDto(user));
        }
        return ResponseEntity.status(401).build();
    }
}