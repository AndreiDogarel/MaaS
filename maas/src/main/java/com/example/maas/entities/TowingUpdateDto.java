package com.example.maas.entities;

import lombok.Value;
import java.time.LocalDate;

@Value
public class TowingUpdateDto {
    LocalDate date;
    String location;
    String reason;
    Integer duration;
}