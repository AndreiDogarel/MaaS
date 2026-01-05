package com.example.maas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "maintenances")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Maintenance {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 128)
    private String type;

    @Column(nullable = false, length = 256)
    private String description;

    @Column(nullable = false)
    private Double cost;

    @Column(name = "is_inspection")
    private Boolean inspection;

    @Column(name = "next_inspection_date")
    private LocalDate nextInspectionDate;

    @Column
    private Long mileage;

    public MaintenanceDto toDto() {
        return MaintenanceDto.builder()
                .id(this.id)
                .date(this.date)
                .type(this.type)
                .description(this.description)
                .cost(this.cost)
                .inspection(this.inspection)
                .nextInspectionDate(this.nextInspectionDate)
                .mileage(this.mileage)
                .build();
    }
}
