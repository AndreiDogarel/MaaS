package com.example.maas.repository;

import com.example.maas.entities.Rental;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByVehicleIdOrderByStartDateDesc(Long vehicleId);
}
