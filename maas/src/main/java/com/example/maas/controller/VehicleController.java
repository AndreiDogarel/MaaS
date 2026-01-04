package com.example.maas.controller;

import com.example.maas.entities.*;
import com.example.maas.service.MaintenanceService;
import com.example.maas.service.RentalService;
import com.example.maas.service.TowingService;
import com.example.maas.service.VehicleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;
    private final MaintenanceService maintenanceService;
    private final TowingService towingService;
    private final RentalService rentalService;

    public VehicleController(
            VehicleService vehicleService,
            MaintenanceService maintenanceService,
            TowingService towingService,
            RentalService rentalService
    ) {
        this.vehicleService = vehicleService;
        this.maintenanceService = maintenanceService;
        this.towingService = towingService;
        this.rentalService = rentalService;
    }

    @PostMapping
    public ResponseEntity<?> addVehicle(@RequestBody CreateVehicleRequest req) {
        if (req == null) {
            return ResponseEntity.badRequest().body("Vehicle payload is required");
        }
        try {
            Vehicle created = vehicleService.create(req);

            VehicleDto dto = new VehicleDto();
            dto.setId(created.getId());
            dto.setRegistrationNumber(created.getRegistrationNumber());
            dto.setBrand(created.getBrand());
            dto.setModel(created.getModel());
            dto.setYear(created.getYear());
            dto.setMileage(created.getMileage());
            dto.setLicenseCategory(created.getLicenseCategory());
            dto.setStatus(created.getStatus());
            dto.setPricePerDay(created.getPricePerDay());

            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to create vehicle");
        }
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
    public ResponseEntity<Towing> addTowing(@PathVariable Long id, @RequestBody TowingDto towing) {
        if (towing == null || towing.getVehicleId() == null || !id.equals(towing.getVehicleId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Towing created = towingService.createTowing(towing);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/maintenance")
    public ResponseEntity<Maintenance> addMaintenance(@PathVariable Long id, @RequestBody MaintenanceDto maintenance) {
        if (maintenance == null || maintenance.getVehicleId() == null || !id.equals(maintenance.getVehicleId())) {
            return ResponseEntity.badRequest().build();
        }
        try {
            Maintenance created = maintenanceService.createMaintenance(maintenance);
            return ResponseEntity.ok(created);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/towing/{towingId}")
    public ResponseEntity<Towing> getTowingById(@PathVariable Long id, @PathVariable Long towingId) {
        return towingService.getTowingById(towingId)
                .filter(t -> t.getVehicle() != null && t.getVehicle().getId().equals(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/towing/{towingId}")
    public ResponseEntity<Towing> updateTowing(@PathVariable Long id, @PathVariable Long towingId, @RequestBody TowingUpdateDto dto) {
        return towingService.getTowingById(towingId)
                .filter(t -> t.getVehicle() != null && t.getVehicle().getId().equals(id))
                .map(existing -> ResponseEntity.ok(towingService.updateTowing(towingId, dto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/towing/{towingId}")
    public ResponseEntity<?> deleteTowing(@PathVariable Long id, @PathVariable Long towingId) {
        return towingService.getTowingById(towingId)
                .filter(t -> t.getVehicle() != null && t.getVehicle().getId().equals(id))
                .map(existing -> towingService.deleteTowing(towingId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}/maintenance/{maintenanceId}")
    public ResponseEntity<Maintenance> getMaintenanceById(@PathVariable Long id, @PathVariable Long maintenanceId) {
        return maintenanceService.getMaintenanceById(maintenanceId)
                .filter(m -> m.getVehicle() != null && m.getVehicle().getId().equals(id))
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/maintenance/{maintenanceId}")
    public ResponseEntity<Maintenance> updateMaintenance(@PathVariable Long id, @PathVariable Long maintenanceId, @RequestBody MaintenanceUpdateDto dto) {
        return maintenanceService.getMaintenanceById(maintenanceId)
                .filter(m -> m.getVehicle() != null && m.getVehicle().getId().equals(id))
                .map(existing -> ResponseEntity.ok(maintenanceService.updateMaintenance(maintenanceId, dto)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/maintenance/{maintenanceId}")
    public ResponseEntity<?> deleteMaintenance(@PathVariable Long id, @PathVariable Long maintenanceId) {
        return maintenanceService.getMaintenanceById(maintenanceId)
                .filter(m -> m.getVehicle() != null && m.getVehicle().getId().equals(id))
                .map(existing -> maintenanceService.deleteMaintenance(maintenanceId) ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public List<VehicleDto> searchVehicles(
            @RequestParam(required = false) String registrationNumber,
            @RequestParam(required = false) String brand,
            @RequestParam(required = false) String model,
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Long mileage,
            @RequestParam(required = false) String licenseCategory,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) Long pricePerDay
    ) {
        VehicleSearchDto searchParams = new VehicleSearchDto(
                registrationNumber,
                brand,
                model,
                year,
                mileage,
                licenseCategory,
                status,
                pricePerDay
        );
        return vehicleService.searchVehicles(searchParams);
    }

    @GetMapping("/{id}/rentals")
    public List<RentalDto> getRentalHistory(@PathVariable Long id) {
        return rentalService.getRentalHistory(id).stream().map(e -> e.toRentalDto()).toList();
    }

    @PostMapping("/{id}/rentals")
    public ResponseEntity<?> addRental(@PathVariable Long id, @RequestBody RentalDto rental) {
        try {
            RentalDto created = rentalService.createRental(id, rental).toRentalDto();
            return ResponseEntity.ok(created);
        } catch (org.springframework.security.access.AccessDeniedException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to create rental");
        }
    }

    @GetMapping("/{id}/rentals/{rentalId}")
    public ResponseEntity<RentalDto> getRentalById(@PathVariable Long id, @PathVariable Long rentalId) {
        return rentalService.getRentalById(rentalId)
                .filter(r -> r.getVehicle() != null && r.getVehicle().getId().equals(id))
                .map(r -> ResponseEntity.ok(r.toRentalDto()))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/rentals/getPending")
    public ResponseEntity<List<RentalContractDto>> getPendingRentals() {
        return ResponseEntity.ok(rentalService.getAllPendingRentals());
    }

    @PutMapping("/{id}/rentals/{rentalId}")
    public ResponseEntity<RentalDto> updateRental(@PathVariable Long id, @PathVariable Long rentalId, @RequestBody RentalUpdateDto dto) {
        System.out.println("Rental ID: " + rentalId);
        return rentalService.getRentalById(rentalId)
                .filter(r -> r.getVehicle() != null && r.getVehicle().getId().equals(id))
                .map(existing -> {
                    Rental updated = rentalService.updateRental(rentalId, dto);
                    return ResponseEntity.ok(updated.toRentalDto());
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}/rentals/{rentalId}")
    public ResponseEntity<?> deleteRental(@PathVariable Long id, @PathVariable Long rentalId) {
        return rentalService.getRentalById(rentalId)
                .filter(r -> r.getVehicle() != null && r.getVehicle().getId().equals(id))
                .map(existing -> rentalService.deleteRental(rentalId)
                        ? ResponseEntity.noContent().build()
                        : ResponseEntity.notFound().build())
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/inspection/overdue")
    public List<VehicleDto> getVehiclesWithOverdueInspection() {
        return vehicleService.getVehiclesWithOverdueInspection();
    }
}
