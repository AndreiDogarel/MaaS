package com.example.maas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id;
    private String registrationNumber;
    private String brand;
    private String model;
    private Integer year;
    private Long mileage;
    private String licenseCategory;
    private String status;
    private Long pricePerDay;
    private List<RentalDto> rentals;
}
