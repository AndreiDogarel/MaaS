package com.example.maas.entities;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "vehicles", uniqueConstraints = {
        @UniqueConstraint(name = "ux_vehicles_registration", columnNames = "registration_number")
})
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Vehicle {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "registration_number", nullable = false, length = 32)
    private String registrationNumber;

    @Column(nullable = false, length = 64)
    private String brand;

    @Column(nullable = false, length = 64)
    private String model;

    @Column(nullable = false)
    private Integer year;

    @Column(nullable = false)
    private Long mileage;

    @Column(name = "license_category", nullable = false, length = 8)
    private String licenseCategory;

    @Column(nullable = false, length = 32)
    private String status;
}
