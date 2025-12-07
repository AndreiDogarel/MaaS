package com.example.maas.repository;

import com.example.maas.entities.Towing;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TowingRepository extends JpaRepository<Towing, Long> {
    List<Towing> findByVehicleId(Long vehicleId);
}
