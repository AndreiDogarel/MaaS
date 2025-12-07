package com.example.maas.entities;

import jakarta.validation.constraints.*;

public record CreateVehicleRequest(
        @NotBlank @Pattern(regexp = "^[A-Z0-9-]{3,32}$") String registrationNumber,
        @NotBlank String brand,
        @NotBlank String model,
        @NotNull @Min(1980) @Max(2100) Integer year,
        @NotNull @Min(0) Long mileage,
        @NotBlank String licenseCategory,     // ex: B, C, D
        @NotBlank String status,          // AVAILABLE, UNAVAILABLE, MAINTENANCE
        @NotBlank Long pricePerDay
) {}

