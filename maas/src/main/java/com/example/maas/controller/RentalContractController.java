package com.example.maas.controller;

import com.example.maas.entities.RentalContractCreateRequest;
import com.example.maas.entities.User;
import com.example.maas.service.RentalContractService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/contracts")
@RequiredArgsConstructor
public class RentalContractController {

    private final RentalContractService rentalContractService;

    @PostMapping
    @PreAuthorize("hasRole('EMPLOYEE') || hasRole('ADMIN')")
    public ResponseEntity<?> createContract(
            @RequestBody RentalContractCreateRequest request
    ) {
        return ResponseEntity.ok(
                rentalContractService.createContract(
                        request.getClientId(),
                        request.getVehicleId(),
                        request.getStartDate(),
                        request.getEndDate()
                )
        );
    }
}

