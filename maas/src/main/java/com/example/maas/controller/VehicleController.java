package com.example.maas.controller;

import com.example.maas.entities.CreateVehicleRequest;
import com.example.maas.entities.Vehicle;
import com.example.maas.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService service;
    public VehicleController(VehicleService service) { this.service = service; }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle create(@RequestBody @Valid CreateVehicleRequest req) {
        return service.create(req);
    }
}

