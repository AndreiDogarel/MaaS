package com.example.maas.entities;

import jakarta.persistence.Column;
import lombok.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceDto { @Column(nullable = false)
    private LocalDate date;
    private String type;
    private String description;
    private Double cost;
    private Long vehicleId;

    public MaintenanceDto(Maintenance maintenance) {
        this.date = maintenance.getDate();
        this.type = maintenance.getType();
        this.description = maintenance.getDescription();
        this.cost = maintenance.getCost();
    }
}
