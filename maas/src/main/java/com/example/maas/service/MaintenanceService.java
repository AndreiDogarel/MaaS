package com.example.maas.service;

import com.example.maas.entities.Maintenance;
import com.example.maas.entities.MaintenanceDto;
import com.example.maas.entities.MaintenanceUpdateDto;
import com.example.maas.entities.Vehicle;
import com.example.maas.repository.MaintenanceRepository;
import com.example.maas.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MaintenanceService {
    private final MaintenanceRepository maintenanceRepository;
    private final VehicleRepository vehicleRepository;

    public MaintenanceService(MaintenanceRepository maintenanceRepository, VehicleRepository vehicleRepository) {
        this.maintenanceRepository = maintenanceRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Maintenance createMaintenance(MaintenanceDto maintenanceDto) {
        Vehicle vehicle = vehicleRepository.findById(maintenanceDto.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + maintenanceDto.getVehicleId()));

        Maintenance entity = Maintenance.builder()
                .date(maintenanceDto.getDate())
                .type(maintenanceDto.getType())
                .description(maintenanceDto.getDescription())
                .cost(maintenanceDto.getCost())
                .inspection(maintenanceDto.getInspection())
                .nextInspectionDate(maintenanceDto.getNextInspectionDate())
                .mileage(maintenanceDto.getMileage())
                .vehicle(vehicle) // Set the fully loaded Vehicle entity
                .build();

        Maintenance saved = maintenanceRepository.save(entity);

        // If a mileage was provided with the maintenance, update the vehicle's mileage
        if (maintenanceDto.getMileage() != null) {
            Long provided = maintenanceDto.getMileage();
            if (vehicle.getMileage() == null || provided >= vehicle.getMileage()) {
                vehicle.setMileage(provided);
                vehicleRepository.save(vehicle);
            }
        }

        return saved;
    }

    public Optional<Maintenance> getMaintenanceById(Long maintenanceId) {
        return maintenanceRepository.findById(maintenanceId);
    }

    public Maintenance updateMaintenance(Long maintenanceId, MaintenanceUpdateDto updateDto) {
        Maintenance existing = maintenanceRepository.findById(maintenanceId)
                .orElseThrow(() -> new EntityNotFoundException("Maintenance not found with ID: " + maintenanceId));

        if (updateDto != null) {
            existing.setDate(updateDto.getDate());
            existing.setType(updateDto.getType());
            existing.setDescription(updateDto.getDescription());
            existing.setCost(updateDto.getCost());
            existing.setInspection(updateDto.getInspection());
            existing.setNextInspectionDate(updateDto.getNextInspectionDate());
            existing.setMileage(updateDto.getMileage());
        }

        Maintenance saved = maintenanceRepository.save(existing);

        // If the update includes a mileage, update the vehicle's mileage as well
        if (updateDto != null && updateDto.getMileage() != null) {
            Vehicle vehicle = saved.getVehicle();
            if (vehicle != null) {
                Long provided = updateDto.getMileage();
                if (vehicle.getMileage() == null || provided >= vehicle.getMileage()) {
                    vehicle.setMileage(provided);
                    vehicleRepository.save(vehicle);
                }
            }
        }

        return saved;
    }

    public boolean deleteMaintenance(Long maintenanceId) {
        if (!maintenanceRepository.existsById(maintenanceId)) {
            return false;
        }
        maintenanceRepository.deleteById(maintenanceId);
        return true;
    }

    public List<Maintenance> getMaintenanceHistory(Long vehicleId) {
        return maintenanceRepository.findByVehicleId(vehicleId);
    }
}
