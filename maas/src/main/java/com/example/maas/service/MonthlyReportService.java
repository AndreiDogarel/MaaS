package com.example.maas.service;

import com.example.maas.entities.MonthlyReport;
import com.example.maas.entities.ReportItem;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MonthlyReportService {
    private final JdbcTemplate jdbc;

    public MonthlyReportService(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public MonthlyReport generateMonthlyReport(Integer yearParam, Integer monthParam) {
        LocalDate now = LocalDate.now();
        int year = (yearParam != null) ? yearParam : now.getYear();
        int month = (monthParam != null) ? monthParam : now.getMonthValue();

        // From Start to End
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.plusMonths(1).minusDays(1);

        java.sql.Date sqlStartDate = java.sql.Date.valueOf(startDate);
        java.sql.Date sqlEndDate = java.sql.Date.valueOf(endDate);

        // Available Vehicles
        Number availableVehiclesNumber = jdbc.queryForObject(
                "SELECT COUNT(*) FROM vehicles WHERE status = 'AVAILABLE'",
                Number.class
        );
        long availableVehicles = (availableVehiclesNumber != null) ? availableVehiclesNumber.longValue() : 0L;

        // Total Service Cost
        Number totalServiceCostNumber = jdbc.queryForObject(
                "SELECT COALESCE(SUM(cost), 0) FROM service_record WHERE date BETWEEN ? AND ?",
                new Object[]{sqlStartDate, sqlEndDate},
                Number.class
        );
        double totalServiceCost = (totalServiceCostNumber != null) ? totalServiceCostNumber.doubleValue() : 0.0;

        List<ReportItem> byType = jdbc.queryForList(
                        "SELECT type AS type, COUNT(*) AS cnt FROM vehicles GROUP BY type"
                )
                .stream()
                .map(row -> {
                    String key = row.get("type") != null ? row.get("type").toString() : "UNKNOWN";
                    Long cnt = row.get("cnt") != null ? ((Number) row.get("cnt")).longValue() : 0L;
                    return new ReportItem(key, cnt);
                })
                .collect(Collectors.toList());

        List<ReportItem> serviceByType = jdbc.queryForList(
                        "SELECT v.type AS type, COALESCE(SUM(s.cost), 0) AS value " +
                                "FROM service_record s JOIN vehicles v ON s.vehicle_id = v.id " +
                                "WHERE s.date BETWEEN ? AND ? GROUP BY v.type",
                        sqlStartDate, sqlEndDate
                )
                .stream()
                .map(
                        row -> {
                            String key = row.get("type") != null ? row.get("type").toString() : "UNKNOWN";
                            Double value = row.get("value") != null ? ((Number) row.get("value")).doubleValue() : 0.0;
                            return new ReportItem(key, 0L, value);
                        }
                )
                .collect(Collectors.toList());


        List<ReportItem> topServiceVehicles = jdbc.queryForList(
                        "SELECT v.id AS id, v.registration_number AS label, COALESCE(SUM(s.cost), 0) AS value " +
                                "FROM service_record s JOIN vehicles v ON s.vehicle_id = v.id " +
                                "WHERE s.date BETWEEN ? AND ? " +
                                "GROUP BY v.id, v.registration_number " +
                                "ORDER BY value DESC " +
                                "LIMIT 5",
                        sqlStartDate, sqlEndDate)
                .stream()
                .map(
                        row -> {
                            String label = row.get("label") != null ? row.get("label").toString() : "UNKNOWN";
                            Double value = row.get("value") != null ? ((Number) row.get("value")).doubleValue() : 0.0;
                            return new ReportItem(label, 0L, value);
                        })
                .collect(Collectors.toList());


        MonthlyReport report = new MonthlyReport();
        report.setYear(year);
        report.setMonth(month);
        report.setAvailableVehicles(availableVehicles);
        report.setTotalServiceCost(totalServiceCost);
        report.setVehiclesByType(byType);
        report.setServiceByType(serviceByType);
        report.setTopServiceVehicles(topServiceVehicles);


        return report;


    }
}
