package com.example.maas.entities;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RentalDto {

    private Long id;
    private Long vehicleId;
    private Long userId;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String status;
    private Long odometerStart;
    private Long odometerEnd;
    private BigDecimal totalPrice;
}
