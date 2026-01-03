package com.example.maas.service;

import com.example.maas.entities.*;
import com.example.maas.repository.RentalRepository;
import com.example.maas.repository.VehicleRepository;
import com.example.maas.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    public List<Rental> getRentalHistory(Long vehicleId) {
        return rentalRepository.findByVehicleIdOrderByStartDateDesc(vehicleId).stream().collect(Collectors.toList());
    }

    public Optional<Rental> getRentalById(Long rentalId) {
        return rentalRepository.findById(rentalId);
    }

    public RentalDto createRental(Long vehicleId, RentalDto dto) {
        if (dto == null) {
            throw new IllegalArgumentException("Rental payload is required");
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new IllegalArgumentException("Vehicle not found"));

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            throw new org.springframework.security.access.AccessDeniedException("Authentication required");
        }

        User actingUser;
        if (authentication.getPrincipal() instanceof User) {
            actingUser = (User) authentication.getPrincipal();
        } else {
            actingUser = userRepository.findByUsername(authentication.getName())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));
        }

        boolean isAdmin = actingUser.getRole() != null
                && actingUser.getRole().getName() == Role.ADMIN;

        boolean isCustomer = actingUser.getRole() != null
                && actingUser.getRole().getName() == Role.CUSTOMER;

        if (isCustomer && !hasValidDocuments.check(actingUser.getId())) {
            throw new AccessDeniedException("User does not have valid documents");
        }

        Rental rental = Rental.builder()
                .vehicle(vehicle)
                .user(actingUser)
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .status(dto.getStatus())
                .odometerStart(dto.getOdometerStart())
                .odometerEnd(dto.getOdometerEnd())
                .totalPrice(dto.getTotalPrice())
                .createdAt(LocalDateTime.now())
                .build();

        return rentalRepository.save(rental).toRentalDto();
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

}
