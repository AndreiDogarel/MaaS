package com.example.maas.service;

import com.example.maas.entities.CreateVehicleRequest;
import com.example.maas.entities.Vehicle;
import com.example.maas.repository.VehicleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class VehicleService {
    private final VehicleRepository repo;

    public VehicleService(VehicleRepository repo) { this.repo = repo; }

    public Vehicle create(CreateVehicleRequest r) {
        if (repo.existsByRegistrationNumber(r.registrationNumber()))
            throw new IllegalArgumentException("registrationNumber already exists");
        if (r.year() < 1980 || r.year() > 2025)
            throw new IllegalArgumentException("year out of range");
        var v = Vehicle.builder()
                .registrationNumber(r.registrationNumber())
                .brand(r.brand())
                .model(r.model())
                .year(r.year())
                .mileage(r.mileage())
                .licenseCategory(r.licenseCategory())
                .status(r.status())
                .build();
        try {
            return repo.save(v);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("duplicate registrationNumber");
        }
    }
}
