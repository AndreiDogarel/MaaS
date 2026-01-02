package com.example.maas.repository;

import com.example.maas.entities.Maintenance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MaintenanceRepository extends JpaRepository<Maintenance, Long> {
    List<Maintenance> findByVehicleId(Long vehicleId);

    @Query("""
        SELECT m.vehicle.id
        FROM Maintenance m
        WHERE m.nextInspectionDate IS NOT NULL
          AND m.nextInspectionDate < CURRENT_DATE
          AND m.date = (
                SELECT MAX(m2.date)
                FROM Maintenance m2
                WHERE m2.vehicle.id = m.vehicle.id
          )
    """)
    List<Long> findVehicleIdsWithOverdueInspection();
}
