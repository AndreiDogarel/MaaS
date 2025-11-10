package com.example.maas.controller;

import com.example.maas.entities.CreateVehicleRequest;
import com.example.maas.entities.Vehicle;
import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleSearchDto;
import com.example.maas.service.VehicleService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {
    private final VehicleService service;
    public VehicleController(VehicleService service) { this.service = service; }

    @GetMapping("/all")
    public List<VehicleDto> getAllVehicles() {
        return service.getAllVehicles();
    }

    @GetMapping("/search")
    public List<VehicleDto> searchVehicles(
            @RequestParam(required = false) String registrationNumber,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long mileage,
            @RequestParam(required = false) String licenseCategory,
            @RequestParam(required = false) String status
    ) {
        VehicleSearchDto searchParams = new VehicleSearchDto(
                registrationNumber,
                brand,
                model,
                year,
                mileage,
                licenseCategory,
                status
        );
        return service.searchVehicles(searchParams);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public Vehicle create(@RequestBody @Valid CreateVehicleRequest req) {
        return service.create(req);
    }
}

