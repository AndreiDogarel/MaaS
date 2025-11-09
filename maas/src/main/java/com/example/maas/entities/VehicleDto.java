package com.example.maas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private String registrationNumber;
    private String brand;
    private String model;
    private Integer year;
    private Long mileage;
    private String licenseCategory;
    private String status;
}
