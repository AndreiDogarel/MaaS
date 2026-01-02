package com.example.maas.controller;

import com.example.maas.entities.Role;
import com.example.maas.repository.RoleRepository;
import com.example.maas.repository.UserRepository;
import com.example.maas.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.example.maas.repository.VehicleRepository;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:4200")
public class AdminController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private VehicleRepository vehicleRepository;

    @DeleteMapping("/deleteUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Map<String, String> deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return Map.of("message", "User deleted successfully");
    }

    @PutMapping("/updateUser/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserDetailsDto updateUser(@PathVariable Long id, @RequestBody UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        if (request.username() != null && !request.username().isBlank()) {
            user.setUsername(request.username());
        }
        if (request.role() != null) {
            user.setRole(roleRepository.getByName(Role.valueOf(request.role())));
        }
        userRepository.save(user);
        return new UserDetailsDto(user.getId(), user.getUsername(), user.getRole().getName().toString());
    }

    @DeleteMapping("/deleteDecommissionedVehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDecommissionedVehicles() {
        vehicleRepository.deleteDecommissionedVehicles();
        return "Decommissioned vehicles deleted successfully";
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserDetailsDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(u -> new UserDetailsDto(
                        u.getId(),
                        u.getUsername(),
                        u.getRole().getName().toString()
                ))
                .toList();
    }

    private record UpdateUserRequest(String username, String role) {}
    private record UserDetailsDto(Long id, String username, String role) {}


}
