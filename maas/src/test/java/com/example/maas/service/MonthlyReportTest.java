package com.example.maas.service;

import com.example.maas.entities.MonthlyReport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MonthlyReportTest {
    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private MonthlyReportService service;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateMonthlyReport_returnsMappedValues() {
        // Arrange: stub queryForObject calls
        when(jdbc.queryForObject(eq("SELECT COUNT(*) FROM vehicles WHERE status = 'AVAILABLE'"), eq(Number.class)))
                .thenReturn(5); // available vehicles

        when(jdbc.queryForObject(startsWith("SELECT COALESCE(SUM(cost)"), any(Object[].class), eq(Number.class)))
                .thenReturn(123.45); // total service cost

        // Arrange stub queryForList for vehicles by type
        Map<String, Object> byTypeRow = new HashMap<>();
        byTypeRow.put("type", "B");
        byTypeRow.put("cnt", 3L);
        List<Map<String, Object>> byTypeList = Collections.singletonList(byTypeRow);
        when(jdbc.queryForList(eq("SELECT license_category AS type, COUNT(*) AS cnt FROM vehicles GROUP BY license_category")))
                .thenReturn(byTypeList);

        // Arrange stub queryForList for service by type (2 date params)
        Map<String, Object> serviceByTypeRow = new HashMap<>();
        serviceByTypeRow.put("type", "B");
        serviceByTypeRow.put("value", 100.0);
        List<Map<String, Object>> serviceByTypeList = Collections.singletonList(serviceByTypeRow);
        when(jdbc.queryForList(startsWith("SELECT v.license_category AS type, COALESCE(SUM(s.cost)"), any(Object[].class))).thenReturn(serviceByTypeList);

        // Arrange stub queryForList for topServiceVehicles (with 2 date params)
        Map<String, Object> topRow = new HashMap<>();
        topRow.put("label", "REG123");
        topRow.put("value", 50.0);
        List<Map<String, Object>> topList = Collections.singletonList(topRow);
        when(jdbc.queryForList(startsWith("SELECT v.id AS id, v.registration_number AS label"), any(Object[].class))).thenReturn(topList);

        // Act: call with explicit year and month
        MonthlyReport report = service.generateMonthlyReport(2023, 5);

        // Assert: verify mapped values
        assertEquals(2023, report.getYear());
        assertEquals(5, report.getMonth());
        assertEquals(5L, report.getAvailableVehicles());
        assertEquals(123.45, report.getTotalServiceCost());
        assertEquals(1, report.getVehiclesByType().size());
        assertEquals("B", report.getVehiclesByType().get(0).getKey());
        assertEquals(3L, report.getVehiclesByType().get(0).getCount());
        assertEquals(1, report.getServiceByType().size());
        assertEquals("B", report.getServiceByType().get(0).getKey());
        assertEquals(100.0, report.getServiceByType().get(0).getValue());
        assertEquals(1, report.getTopServiceVehicles().size());
        assertEquals("REG123", report.getTopServiceVehicles().get(0).getKey());
        assertEquals(50.0, report.getTopServiceVehicles().get(0).getValue());


    }
}
