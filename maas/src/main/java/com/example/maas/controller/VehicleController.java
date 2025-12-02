package com.example.maas.controller;

import com.example.maas.entities.*;
import com.example.maas.service.MaintenanceService;
import com.example.maas.service.TowingService;
import com.example.maas.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final MaintenanceService maintenanceService;
    private final TowingService towingService;

    public VehicleController(VehicleService vehicleService, MaintenanceService maintenanceService, TowingService towingService) {
        this.vehicleService = vehicleService;
        this.maintenanceService = maintenanceService;
        this.towingService = towingService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<VehicleDto> getVehicleById(@PathVariable Long id) {
        return vehicleService.getVehicleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/maintenance")
    public List<Maintenance> getMaintenanceHistory(@PathVariable Long id) {
        return maintenanceService.getMaintenanceHistory(id);
    }

    @GetMapping("/{id}/towing")
    public List<Towing> getTowingHistory(@PathVariable Long id) {
        return towingService.getTowingHistory(id);
    }

    @PostMapping("/{id}/towing")
    public Towing addTowing(@RequestBody TowingDto towing) {
        return towingService.createTowing(towing);
    }

    @PostMapping("/{id}/maintenance")
    public Maintenance addMaintenance(@RequestBody MaintenanceDto maintenance) {
        return maintenanceService.createMaintenance(maintenance);
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
        return vehicleService.searchVehicles(searchParams);
    }
}
