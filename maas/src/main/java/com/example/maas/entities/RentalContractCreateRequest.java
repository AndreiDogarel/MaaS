package com.example.maas.entities;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalContractCreateRequest {

    private Long clientId;
    private Long vehicleId;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long operatorId;

    // getters & setters
}
