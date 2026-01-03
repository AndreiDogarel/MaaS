package com.example.maas.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "rentals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Rental {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(nullable = false, length = 32)
    private String status;

    @Column(name = "odometer_start")
    private Long odometerStart;

    @Column(name = "odometer_end")
    private Long odometerEnd;

    @Column(name = "total_price", precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    public RentalDto toRentalDto(){
        System.out.println(this.getUser().getId());
        return RentalDto.builder()
                .id(this.getId())
                .startDate(this.getStartDate())
                .endDate(this.getEndDate())
                .status(this.getStatus())
                .odometerStart(this.getOdometerStart())
                .odometerEnd(this.getOdometerEnd())
                .totalPrice(this.getTotalPrice())
                .build();
    }
}
