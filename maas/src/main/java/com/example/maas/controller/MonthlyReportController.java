package com.example.maas.controller;

import com.example.maas.entities.MonthlyReport;
import com.example.maas.service.MonthlyReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/reports")
public class MonthlyReportController {
    private final MonthlyReportService monthlyReportService;

    public MonthlyReportController(MonthlyReportService monthlyReportService) {
        this.monthlyReportService = monthlyReportService;
    }

    @GetMapping("/monthly-reports")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<MonthlyReport> monthlyReport(
            @RequestParam(required = false) Integer year,
            @RequestParam(required = false) Integer month) {
        MonthlyReport report = monthlyReportService.generateMonthlyReport(year, month);
        return ResponseEntity.ok(report);
    }
}
