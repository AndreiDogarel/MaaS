package com.example.maas.entities;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MonthlyReport {
    private int year;
    private int month;
    private long availableVehicles;
    private double totalServiceCost;
    private List<ReportItem> vehiclesByType;
    private List<ReportItem> serviceByType;
    private List<ReportItem> topServiceVehicles;

}
