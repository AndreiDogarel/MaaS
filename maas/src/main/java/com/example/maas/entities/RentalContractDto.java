package com.example.maas.entities;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalContractDto {
    private String registrationPlate;
    private LocalDate startDate;
    private LocalDate endDate;
    private String username;
}
