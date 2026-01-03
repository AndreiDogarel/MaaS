package com.example.maas.entities;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalContractResponseDto {
    private Long clientId;
    private String vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long operatorId;
}
