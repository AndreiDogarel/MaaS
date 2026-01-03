package com.example.maas.repository;


import com.example.maas.entities.RentalContract;
import com.example.maas.entities.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface RentalContractRepository extends JpaRepository<RentalContract, Long> {

    boolean existsByVehicleAndEndDateAfterAndStartDateBefore(
            Vehicle vehicle,
            LocalDate startDate,
            LocalDate endDate
    );
}

