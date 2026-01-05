package com.example.maas.entities;

import lombok.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TowingDto {
    private Long id;
    private LocalDate date;
    private String location;
    private String reason;
    private Integer duration;
    private Long vehicleId;

    public TowingDto(Towing towing) {
        this.id = towing.getId();
        this.date = towing.getDate();
        this.location = towing.getLocation();
        this.reason = towing.getReason();
        this.duration = towing.getDuration();
        this.vehicleId = towing.getVehicle().getId();
    }
}
