package com.example.maas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalContract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate startDate;
    private LocalDate endDate;

    private Double totalPrice;

    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Vehicle vehicle;

    @ManyToOne
    private User operator;

    // getters & setters
    public RentalContractResponseDto toResponseDto(Long clientId){
        return RentalContractResponseDto.builder()
                .clientId(user.getId())
                .vehicleId(vehicle.getRegistrationNumber())
                .startDate(startDate)
                .endDate(endDate)
                .operatorId(clientId)
                .build();
    }
}

