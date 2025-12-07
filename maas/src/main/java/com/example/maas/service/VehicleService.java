package com.example.maas.service;

import com.example.maas.entities.CreateVehicleRequest;
import com.example.maas.entities.Vehicle;
import com.example.maas.entities.VehicleDto;
import com.example.maas.entities.VehicleSearchDto;
import com.example.maas.repository.VehicleRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleService {

    private final VehicleRepository repo;
    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<VehicleDto> vehicleRowMapper = (rs, rowNum) -> {
        VehicleDto vehicle = new VehicleDto();
        vehicle.setId(rs.getObject("id", Long.class));
        vehicle.setRegistrationNumber(rs.getObject("registration_number", String.class));
        vehicle.setBrand(rs.getObject("brand", String.class));
        vehicle.setModel(rs.getObject("model", String.class));
        vehicle.setYear(rs.getObject("year", Integer.class));
        vehicle.setMileage(rs.getObject("mileage", Long.class));
        vehicle.setLicenseCategory(rs.getString("license_category"));
        vehicle.setStatus(rs.getString("status"));
        vehicle.setPricePerDay(rs.getLong("price_per_day"));
        return vehicle;
    };

    public VehicleService(VehicleRepository repo, JdbcTemplate jdbcTemplate) {
        this.repo = repo;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<VehicleDto> getAllVehicles() {
        String sql = "SELECT * FROM vehicles";
        return jdbcTemplate.query(sql, vehicleRowMapper);
    }

    public Optional<VehicleDto> getVehicleById(Long id) {
        String sql = "SELECT * FROM vehicles WHERE id = ?";
        return jdbcTemplate.query(sql, new Object[]{id}, vehicleRowMapper)
                .stream()
                .findFirst();
    }

    public Vehicle create(CreateVehicleRequest r) {
        if (repo.existsByRegistrationNumber(r.registrationNumber())) {
            throw new IllegalArgumentException("registrationNumber already exists");
        }
        if (r.year() < 1980 || r.year() > 2025) {
            throw new IllegalArgumentException("year out of range");
        }

        Vehicle v = Vehicle.builder()
                .registrationNumber(r.registrationNumber())
                .brand(r.brand())
                .model(r.model())
                .year(r.year())
                .mileage(r.mileage())
                .licenseCategory(r.licenseCategory())
                .status(r.status())
                .pricePerDay(r.pricePerDay())
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

        if (searchParams.getRegistrationNumber() != null && !searchParams.getRegistrationNumber().isBlank()) {
            sql.append(" AND registration_number ILIKE ?");
            args.add("%" + searchParams.getRegistrationNumber() + "%");
        }

        if (searchParams.getBrand() != null && !searchParams.getBrand().isBlank()) {
            sql.append(" AND brand ILIKE ?");
            args.add("%" + searchParams.getBrand() + "%");
        }

        if (searchParams.getModel() != null && !searchParams.getModel().isBlank()) {
            sql.append(" AND model ILIKE ?");
            args.add("%" + searchParams.getModel() + "%");
        }

        if (searchParams.getLicenseCategory() != null && !searchParams.getLicenseCategory().isBlank()) {
            sql.append(" AND license_category = ?");
            args.add(searchParams.getLicenseCategory());
        }

        if (searchParams.getStatus() != null && !searchParams.getStatus().isBlank()) {
            sql.append(" AND status = ?");
            args.add(searchParams.getStatus());
        }

        if (searchParams.getYear() != null) {
            sql.append(" AND year >= ?");
            args.add(searchParams.getYear());
        }

        if (searchParams.getPricePerDay() != null) {
            sql.append(" AND price_per_day >= ?");
            args.add(searchParams.getPricePerDay());
        }

        return jdbcTemplate.query(sql.toString(), args.toArray(), vehicleRowMapper);
    }
}
