package com.example.maas.entities;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReportItem {
    private String key;
    private long count;
    private double value;

    public ReportItem(){}

    public ReportItem(String key, long count) {
        this.key = key;
        this.count = count;
    }

    public ReportItem(String key, long count, double value) {
        this.key = key;
        this.count = count;
        this.value = value;
    }
}
