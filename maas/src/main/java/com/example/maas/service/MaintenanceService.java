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
                .vehicle(vehicle) // Set the fully loaded Vehicle entity
                .build();

        return maintenanceRepository.save(entity);
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
        }

        return maintenanceRepository.save(existing);
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
