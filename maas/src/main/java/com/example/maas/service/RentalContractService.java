package com.example.maas.service;


import com.example.maas.entities.*;
import com.example.maas.repository.RentalContractRepository;
import com.example.maas.repository.RentalRepository;
import com.example.maas.repository.UserRepository;
import com.example.maas.repository.VehicleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class RentalContractService {

    private final RentalContractRepository contractRepository;
    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;

    public RentalContractResponseDto createContract(
            String registrationNumber,
            LocalDate start,
            LocalDate end
    ) {
        Vehicle vehicle = vehicleRepository.findByRegistrationNumber(registrationNumber)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        Rental rental = rentalRepository.findRentalByStartDateAndVehicle(start, vehicle);

        boolean exists = contractRepository
                .existsByVehicleAndEndDateAfterAndStartDateBefore(vehicle, start, end);

        if (exists) {
            throw new IllegalStateException("Contract for this vehicle already exists");
        }

        if (rental == null) {
            throw new RuntimeException("Rental not found");
        }
        RentalContract contract = new RentalContract();
        contract.setVehicle(vehicle);
        contract.setUser(rental.getUser());
        contract.setStartDate(start);
        contract.setEndDate(end);
        contract.setCreatedAt(LocalDateTime.now());
        contract.setOperator(userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        contractRepository.save(contract);
        rental.setStatus(RentalStatus.ACTIVE);
        rentalRepository.save(rental);
        System.out.println(contract.getOperator().getUsername());
        System.out.println(contract.getOperator().getId());
        return contract.toResponseDto(contract.getOperator().getId());
    }
}

