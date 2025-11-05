package com.example.maas.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VehicleDto {
    private Long id; // Used to identify the vehicle
    private Long mileage;
    private String status;
    private String licenseCategory;
}
