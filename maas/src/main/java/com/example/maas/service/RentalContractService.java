package com.example.maas.service;


import com.example.maas.entities.*;
import com.example.maas.repository.RentalContractRepository;
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

    public RentalContractResponseDto createContract(
            Long clientId,
            Long vehicleId,
            LocalDate start,
            LocalDate end
    ) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new RuntimeException("Vehicle not found"));

        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client not found"));

        boolean exists = contractRepository
                .existsByVehicleAndEndDateAfterAndStartDateBefore(vehicle, start, end);

        if (exists) {
            throw new IllegalStateException("Vehicle already rented in this period");
        }

        RentalContract contract = new RentalContract();
        contract.setVehicle(vehicle);
        contract.setUser(client);
        contract.setStartDate(start);
        contract.setEndDate(end);
        contract.setCreatedAt(LocalDateTime.now());
        contract.setOperator(userRepository.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName()));

        contractRepository.save(contract);
        System.out.println(contract.getOperator().getUsername());
        System.out.println(contract.getOperator().getId());
        return contract.toResponseDto(contract.getOperator().getId());
    }
}

