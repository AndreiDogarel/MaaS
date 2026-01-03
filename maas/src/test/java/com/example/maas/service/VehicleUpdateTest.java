package com.example.maas.service;

import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleUpdateDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VehicleUpdateTest {
    @Mock
    private JdbcTemplate jdbc;

    @InjectMocks
    private VehicleUpdateService service;

    @Test
    void getVehicleById_returnsVehicle_whenExists() {
        Long vehicleId = 1L;
        VehicleDto vehicle = new VehicleDto();
        vehicle.setRegistrationNumber("B-970-XIT");

        // Mocking the jdbc queryForObject method
        when(jdbc.queryForObject(anyString(), any(RowMapper.class), eq(vehicleId)))
                .thenReturn(vehicle);

        VehicleDto result = service.getVehicleById(vehicleId);
        assert(result != null);
        assert(result.getRegistrationNumber().equals("B-970-XIT"));
    }

    @Test
    void getVehicleById_returnsNull_whenNotExists() {
        // Arrange
        when(jdbc.queryForObject(anyString(), any(RowMapper.class), anyLong()))
                .thenThrow(new EmptyResultDataAccessException(1));

        // Act
        VehicleDto result = service.getVehicleById(999L);

        // Assert
        assert(result == null);
    }

    @Test
    void updateVehicle_callsJdbcUpdate() {
        // Arrange
        Long vehicleId = 10L;
        VehicleUpdateDto updateDto = new VehicleUpdateDto();
        updateDto.setMileage(5000L);
        updateDto.setStatus("SERVICE");
        updateDto.setLicenseCategory("B");

        // Mock the update method to return 1 row affected
        when(jdbc.update(anyString(), eq(5000L), eq("SERVICE"), eq("B"), eq(vehicleId)))
                .thenReturn(1);

        // Act
        int rowsAffected = service.updateVehicle(vehicleId, updateDto);

        // Assert
        assertEquals(1, rowsAffected);
    }


}
