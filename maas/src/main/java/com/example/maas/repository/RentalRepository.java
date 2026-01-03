package com.example.maas.repository;

import com.example.maas.entities.Rental;
import com.example.maas.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface RentalRepository extends JpaRepository<Rental, Long> {

    List<Rental> findByVehicleIdOrderByStartDateDesc(Long vehicleId);

    Rental findRentalByStartDateAndVehicle(LocalDate startDate, Vehicle vehicle);
}
