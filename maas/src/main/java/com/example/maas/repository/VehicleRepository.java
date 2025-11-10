package com.example.maas.repository;

import com.example.maas.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
    boolean existsByRegistrationNumber(String registrationNumber);

     Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    Optional<Vehicle> findById(Long id);

    List<Vehicle> findByLicenseCategory(String licenseCategory);
    List<Vehicle> findByBrand(String brand);
    List<Vehicle> findByModel(String model);
}
