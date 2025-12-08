package com.example.maas.service;

import com.example.maas.entities.Rental;
import com.example.maas.entities.RentalDto;
import com.example.maas.entities.RentalUpdateDto;
import com.example.maas.entities.Vehicle;
import com.example.maas.entities.User;
import com.example.maas.repository.RentalRepository;
import com.example.maas.repository.VehicleRepository;
import com.example.maas.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalRepository rentalRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final HasValidDocuments hasValidDocuments;

    public RentalService(RentalRepository rentalRepository,
                         VehicleRepository vehicleRepository,
                         UserRepository userRepository,
                            HasValidDocuments hasValidDocuments
    ) {
        this.rentalRepository = rentalRepository;
        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
        this.hasValidDocuments = hasValidDocuments;
    }

    public List<RentalDto> getRentalHistoryForVehicle(Long vehicleId) {
        return rentalRepository.findByVehicleIdOrderByStartDateDesc(vehicleId)
                .stream()
                .map(this::toDto)
                .toList();
    }

    public List<Rental> getRentalHistory(Long vehicleId) {
        return rentalRepository.findByVehicleIdOrderByStartDateDesc(vehicleId);
    }

    public Optional<Rental> getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId);
    }

    public Rental createRental(RentalDto dto) {
        Vehicle vehicle = vehicleRepository.findById(dto.getVehicleId())
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

//        Check if user has valid documents
        Long userId = dto.getUserId();
        if (!this.hasValidDocuments.check(userId)) {
            throw new org.springframework.security.access.AccessDeniedException("User does not have valid documents");
        }

        Rental rental = Rental.builder()
                .vehicle(vehicle)
                .user(user)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .odometerStart(dto.getOdometerStart())
                .odometerEnd(dto.getOdometerEnd())
                .totalPrice(dto.getTotalPrice())
                .createdAt(dto.getStartDate() != null ? dto.getStartDate() : LocalDateTime.now())
                .build();

        return rentalRepository.save(rental);
    }

    public Rental updateRental(Long rentalId, RentalUpdateDto dto) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new IllegalArgumentException("Rental not found"));

        if (dto.getStartDate() != null) {
            rental.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate() != null) {
            rental.setEndDate(dto.getEndDate());
        }
        if (dto.getStatus() != null && !dto.getStatus().isBlank()) {
            rental.setStatus(dto.getStatus());
        }
        if (dto.getOdometerStart() != null) {
            rental.setOdometerStart(dto.getOdometerStart());
        }
        if (dto.getOdometerEnd() != null) {
            rental.setOdometerEnd(dto.getOdometerEnd());
        }
        if (dto.getTotalPrice() != null) {
            rental.setTotalPrice(dto.getTotalPrice());
        }

        return rentalRepository.save(rental);
    }

    public boolean deleteRental(Long rentalId) {
        if (!rentalRepository.existsById(rentalId)) {
            return false;
        }
        rentalRepository.deleteById(rentalId);
        return true;
    }

    private RentalDto toDto(Rental r) {
        return RentalDto.builder()
                .id(r.getId())
                .vehicleId(r.getVehicle().getId())
                .userId(r.getUser().getId())
                .startDate(r.getStartDate())
                .endDate(r.getEndDate())
                .status(r.getStatus())
                .odometerStart(r.getOdometerStart())
                .odometerEnd(r.getOdometerEnd())
                .totalPrice(r.getTotalPrice())
                .build();
    }
}
