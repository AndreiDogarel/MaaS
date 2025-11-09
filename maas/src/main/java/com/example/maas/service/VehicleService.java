package com.example.maas.service;

import com.example.maas.entities.CreateVehicleRequest;
import com.example.maas.entities.Vehicle;
import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleSearchDto;
import com.example.maas.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository repo;
    @Autowired
    private JdbcTemplate jdbcTemplate;

    public VehicleService(VehicleRepository repo) { this.repo = repo; }

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

    public List<VehicleDto> getAllVehicles() {
        String sql = "SELECT * FROM vehicles";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    public Vehicle create(CreateVehicleRequest r) {
        if (repo.existsByRegistrationNumber(r.registrationNumber()))
            throw new IllegalArgumentException("registrationNumber already exists");
        if (r.year() < 1980 || r.year() > 2025)
            throw new IllegalArgumentException("year out of range");
        var v = Vehicle.builder()
                .registrationNumber(r.registrationNumber())
                .brand(r.brand())
                .model(r.model())
                .year(r.year())
                .mileage(r.mileage())
                .licenseCategory(r.licenseCategory())
                .status(r.status())
                .build();
        try {
            return repo.save(v);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalArgumentException("duplicate registrationNumber");
        }
    }

    public List<VehicleDto> searchVehicles(VehicleSearchDto searchParams) {
        StringBuilder sql = new StringBuilder("SELECT * FROM vehicles WHERE 1=1");
        List<Object> args = new java.util.ArrayList<>();

        // Add brand filter
        if (searchParams.getBrand() != null && !searchParams.getBrand().isBlank()) {
            sql.append(" AND brand ILIKE ?"); // ILIKE for case-insensitive search in PostgreSQL
            args.add("%" + searchParams.getBrand() + "%");
        }

        // Add model filter
        if (searchParams.getModel() != null && !searchParams.getModel().isBlank()) {
            sql.append(" AND model ILIKE ?");
            args.add("%" + searchParams.getModel() + "%");
        }

        // Add license category filter
        if (searchParams.getLicenseCategory() != null && !searchParams.getLicenseCategory().isBlank()) {
            sql.append(" AND license_category = ?");
            args.add(searchParams.getLicenseCategory());
        }

        // Add status filter
        if (searchParams.getStatus() != null && !searchParams.getStatus().isBlank()) {
            sql.append(" AND status = ?");
            args.add(searchParams.getStatus());
        }

        // Add year filter (e.g., year greater than or equal to)
        if (searchParams.getYear() != null) {
            sql.append(" AND year >= ?");
            args.add(searchParams.getYear());
        }

        // Execute the query using the dynamic SQL string and arguments
        return jdbcTemplate.query(sql.toString(), args.toArray(), vehicleRowMapper);
    }
}
