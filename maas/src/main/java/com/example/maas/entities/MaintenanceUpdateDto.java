package com.example.maas.entities;

import lombok.*;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceUpdateDto {
    private LocalDate date;
    private String type;
    private String description;
    private Double cost;
}
