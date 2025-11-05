package com.example.maas.controller;

import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleUpdateDto;
import com.example.maas.service.VehicleUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/admin/vehicle")
@CrossOrigin(origins = "http://localhost:4200")
public class VehicleUpdateController {
    @Autowired
    private VehicleUpdateService vehicleUpdateService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')") // Maybe remove admin restriction later
    public List<VehicleDto> getAllVehicles() {
        return vehicleUpdateService.getAllVehicles();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')") // Maybe remove admin restriction later
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable Long id) {
        VehicleDto vehicle = vehicleUpdateService.getVehicleById(id);
        if (vehicle != null) {
            return ResponseEntity.ok(vehicle);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/update/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VehicleUpdateDto> updateVehicle(@PathVariable Long id, @RequestBody VehicleUpdateDto vehicleUpdateDto) {
        int rowsAffected = vehicleUpdateService.updateVehicle(id, vehicleUpdateDto);
        if (rowsAffected > 0) {
            return ResponseEntity.ok(vehicleUpdateDto);
        } else {
            return ResponseEntity.notFound().build();
        }

    }

}
