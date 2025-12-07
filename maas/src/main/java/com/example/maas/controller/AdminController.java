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
    public String deleteUser(@PathVariable("id") Long id) {
        userRepository.deleteById(id);
        return "User deleted successfully";
    }

    @PutMapping("/updateUserRole/{id}/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUserRole(@PathVariable Long id, @PathVariable Role role) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(roleRepository.getByName(role));
        userRepository.save(user);
        return "User role updated successfully";
    }

    @PutMapping("/resetPassword/{id}/{newPassword}")
    @PreAuthorize("hasRole('ADMIN')")
    public String resetPassword(@PathVariable Long id, @PathVariable String newPassword) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        return "Password reset successfully";
    }

    @PutMapping("/updateUsername/{id}/{newUsername}")
    @PreAuthorize("hasRole('ADMIN')")
    public String updateUsername(@PathVariable Long id, @PathVariable String newUsername) {
        var user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        user.setUsername(newUsername);
        userRepository.save(user);
        return "Username updated successfully";
    }

    @DeleteMapping("/deleteDecommissionedVehicles")
    @PreAuthorize("hasRole('ADMIN')")
    public String deleteDecommissionedVehicles() {
        vehicleRepository.deleteDecommissionedVehicles();
        return "Decommissioned vehicles deleted successfully";
    }
}
