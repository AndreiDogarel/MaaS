package com.example.maas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "towings")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Towing {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, length = 128)
    private String location;

    @Column(nullable = false, length = 256)
    private String reason;

    @Column(nullable = false)
    private Integer duration;

    public TowingDto toDto() {
        return TowingDto.builder()
                .id(this.id)
                .date(this.date)
                .location(this.location)
                .reason(this.reason)
                .duration(this.duration)
                .build();
    }
}