package com.example.maas.entities;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleUpdateDto {

    private String registrationNumber;
    private String brand;
    private String model;
    private Integer year;
    private Long mileage;
    private String licenseCategory;
    private String status;
    private Long pricePerDay;
}
