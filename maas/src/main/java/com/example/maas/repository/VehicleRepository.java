package com.example.maas.repository;

import com.example.maas.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {

    boolean existsByRegistrationNumber(String registrationNumber);

    Optional<Vehicle> findByRegistrationNumber(String registrationNumber);
    Optional<Vehicle> findById(Long id);

    List<Vehicle> findByLicenseCategory(String licenseCategory);
    List<Vehicle> findByBrand(String brand);
    List<Vehicle> findByModel(String model);

    @Transactional
    @Modifying
    @Query(value = """
        DELETE FROM vehicles
        WHERE status = 'UNAVAILABLE'
          AND (mileage > 100000 OR (EXTRACT(YEAR FROM CURRENT_DATE) - year) > 10)
    """, nativeQuery = true)
    void deleteDecommissionedVehicles();


    List<Vehicle> findVehicleById(Long id);
}
