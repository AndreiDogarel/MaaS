package com.example.maas.entities;


import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalContractCreateRequest {

    private String registrationNumber;
    private LocalDate startDate;
    private LocalDate endDate;

    // getters & setters
}
