package com.example.maas.service;

import com.example.maas.entities.Towing;
import com.example.maas.entities.TowingDto;
import com.example.maas.entities.TowingUpdateDto;
import com.example.maas.entities.Vehicle;
import com.example.maas.repository.TowingRepository;
import com.example.maas.repository.VehicleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TowingService {
    private final TowingRepository towingRepository;
    private final VehicleRepository vehicleRepository;

    public TowingService(TowingRepository towingRepository, VehicleRepository vehicleRepository) {
        this.towingRepository = towingRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<TowingDto> getTowingHistory(Long vehicleId) {
        return towingRepository.findByVehicleId(vehicleId).stream().map(Towing::toDto).collect(Collectors.toList());
    }

    public Optional<Towing> getTowingById(Long towingId) {
        return towingRepository.findById(towingId);
    }

    public Towing createTowing(TowingDto towingDto) {
        Vehicle vehicle = vehicleRepository.findById(towingDto.getVehicleId())
                .orElseThrow(() -> new EntityNotFoundException("Vehicle not found with ID: " + towingDto.getVehicleId()));

        Towing entity = Towing.builder()
                .date(towingDto.getDate())
                .location(towingDto.getLocation())
                .reason(towingDto.getReason())
                .duration(towingDto.getDuration())
                .vehicle(vehicle) // Set the fully loaded Vehicle entity
                .build();

        return towingRepository.save(entity);
    }

    public Towing updateTowing(Long towingId, TowingUpdateDto updateDto) {
        Towing existingTowing = towingRepository.findById(towingId)
                .orElseThrow(() -> new EntityNotFoundException("Towing not found with ID: " + towingId));

        existingTowing.setDate(updateDto.getDate());
        existingTowing.setLocation(updateDto.getLocation());
        existingTowing.setReason(updateDto.getReason());
        existingTowing.setDuration(updateDto.getDuration());

        return towingRepository.save(existingTowing);
    }

    public boolean deleteTowing(Long towingId) {
        if (!towingRepository.existsById(towingId)) {
            return false;
        }
        towingRepository.deleteById(towingId);
        return true;
    }
}
