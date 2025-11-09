package com.example.maas.service;

import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleUpdateDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleUpdateService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    // TODO: Fix vehicles database once it exists
    private final RowMapper<VehicleDto> vehicleRowMapper = (rs, rowNum) -> {
        VehicleDto vehicle = new VehicleDto();
        vehicle.setRegistrationNumber(rs.getObject("registration_number", String.class));
        vehicle.setBrand(rs.getObject("brand", String.class));
        vehicle.setModel(rs.getObject("model", String.class));
        vehicle.setYear(rs.getObject("year", Integer.class));
        vehicle.setMileage(rs.getObject("mileage", Long.class));
        vehicle.setLicenseCategory(rs.getString("license_category"));
        vehicle.setStatus(rs.getString("status"));
        return vehicle;
    };

    public VehicleDto getVehicleById(Long id) {
        String sql = "SELECT registration_number, brand, model, mileage, status, license_category FROM vehicles WHERE id = ?";
        try {
            return jdbcTemplate.queryForObject(sql, vehicleRowMapper, id);
        } catch (EmptyResultDataAccessException e) {
            return null; // Return null if not found
        }
    }

    public int updateVehicle(Long id, VehicleUpdateDto vehicleUpdateDto) {
        String sql = "UPDATE vehicles SET mileage = ?, status = ?, license_category = ? WHERE id = ?";
        return jdbcTemplate.update(sql,
                vehicleUpdateDto.getMileage(),
                vehicleUpdateDto.getStatus(),
                vehicleUpdateDto.getLicenseCategory(),
                id);
    }

}
