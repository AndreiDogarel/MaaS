package com.example.maas.service;

import com.example.maas.entities.Vehicle;
import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleSearchDto;
import com.example.maas.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleServiceTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private JdbcTemplate jdbcTemplate;

    @InjectMocks
    private VehicleService vehicleService;

    private VehicleDto vehicle1;
    private VehicleDto vehicle2;
    private VehicleDto vehicle3;

    @BeforeEach
    void setUp() {
        vehicle1 = new VehicleDto();
        vehicle1.setId(1L);
        vehicle1.setRegistrationNumber("REG123");
        vehicle1.setBrand("Toyota");
        vehicle1.setModel("Camry");
        vehicle1.setYear(2020);
        vehicle1.setMileage(50000L);
        vehicle1.setLicenseCategory("B");
        vehicle1.setStatus("Unavailable");

        vehicle2 = new VehicleDto();
        vehicle2.setId(2L);
        vehicle2.setRegistrationNumber("XYZ789");
        vehicle2.setBrand("Honda");
        vehicle2.setModel("Civic");
        vehicle2.setYear(2018);
        vehicle2.setMileage(75000L);
        vehicle2.setLicenseCategory("B");
        vehicle2.setStatus("Maintenance");

        vehicle3 = new VehicleDto();
        vehicle3.setId(3L);
        vehicle3.setRegistrationNumber("ABC456");
        vehicle3.setBrand("Mercedes");
        vehicle3.setModel("C-Class");
        vehicle3.setYear(2022);
        vehicle3.setMileage(25000L);
        vehicle3.setLicenseCategory("B");
        vehicle3.setStatus("Available");
    }

    @Test
    void getVehicleById_shouldReturnVehicle_whenFound() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(vehicle1));

        Optional<VehicleDto> foundVehicle = vehicleService.getVehicleById(1L);

        assertTrue(foundVehicle.isPresent());
        assertEquals(vehicle1.getId(), foundVehicle.get().getId());
    }

    @Test
    void getVehicleById_shouldReturnEmpty_whenNotFound() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList());

        Optional<VehicleDto> foundVehicle = vehicleService.getVehicleById(99L);

        assertTrue(foundVehicle.isEmpty());
    }

    @Test
    void search_shouldReturnMatchingVehicles_byRegistrationNumber() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(vehicle1));

        VehicleSearchDto params = new VehicleSearchDto("REG123", null, null, null, null, null, null, null);
        List<VehicleDto> result = vehicleService.searchVehicles(params);

        assertEquals(1, result.size());
        assertEquals("REG123", result.get(0).getRegistrationNumber());
    }

    @Test
    void search_shouldReturnMatchingVehicles_byBrand() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(vehicle1));

        VehicleSearchDto params = new VehicleSearchDto(null, "Toyota", null, null, null, null, null, null);
        List<VehicleDto> result = vehicleService.searchVehicles(params);

        assertEquals(1, result.size());
        assertEquals("Toyota", result.get(0).getBrand());
    }

    @Test
    void search_shouldReturnMatchingVehicles_byModel() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList(vehicle1));

        VehicleSearchDto params = new VehicleSearchDto(null, null, "Camry", null, null, null, null, null);
        List<VehicleDto> result = vehicleService.searchVehicles(params);

        assertEquals(1, result.size());
        assertEquals("Camry", result.get(0).getModel());
    }

    @Test
    void search_shouldReturnEmptyList_whenNoMatch() {
        when(jdbcTemplate.query(anyString(), any(Object[].class), any(RowMapper.class)))
                .thenReturn(Arrays.asList());

        VehicleSearchDto params = new VehicleSearchDto(null, null, null, null, null, null, "NonExistent", null);
        List<VehicleDto> result = vehicleService.searchVehicles(params);

        assertTrue(result.isEmpty());
    }
}
