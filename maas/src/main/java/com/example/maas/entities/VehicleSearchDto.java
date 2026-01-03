package com.example.maas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleSearchDto {
    private String registrationNumber;
    private String brand;
    private String model;
    private Integer year;
    private Long mileage;
    private String licenseCategory;
    private String status;
    private Long pricePerDay;

    public VehicleSearchDto(String reg123, Object o, Object o1, Object o2, Object o3, Object o4, Object o5) {
    }
}
