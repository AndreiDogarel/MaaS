package com.example.maas.service;

import com.example.maas.entities.*;
import com.example.maas.repository.RentalContractRepository;
import com.example.maas.repository.RentalRepository;
import com.example.maas.repository.UserRepository;
import com.example.maas.repository.VehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalContractServiceTest {

    @Mock
    private RentalContractRepository contractRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private RentalContractService service;

    @AfterEach
    void cleanupSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void createContract_success_savesAndReturnsResponseDto() {
        // Arrange
        Long clientId = 11L;
        String registrationNumber = "DB95XDR";
        LocalDate start = LocalDate.of(2026, 1, 10);
        LocalDate end = LocalDate.of(2026, 1, 15);

        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(registrationNumber);
        vehicle.setRegistrationNumber("ABC-123");

        User client = new User();
        client.setId(clientId);

        User operator = new User();
        operator.setId(99L);
        operator.setUsername("opUser");

        Rental rental = Rental.builder()
                .vehicle(vehicle)
                .user(client)
                .startDate(start)
                .endDate(end)
                .status(RentalStatus.ACTIVE)
                .createdAt(LocalDateTime.now())
                .build();

        when(vehicleRepository.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.of(vehicle));
//        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepository.existsByVehicleAndEndDateAfterAndStartDateBefore(eq(vehicle), eq(start), eq(end)))
                .thenReturn(false);

        // set SecurityContext so service can get the authenticated username
        SecurityContext ctx = new SecurityContextImpl();
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn(operator.getUsername());
        ctx.setAuthentication(auth);
        SecurityContextHolder.setContext(ctx);

        when(userRepository.getUserByUsername(operator.getUsername())).thenReturn(operator);
        when(rentalRepository.findRentalByStartDateAndVehicle(any(), any()))
                .thenReturn(rental);
        // capture the saved contract and return it (simulate JPA saving and returning with fields set)
        ArgumentCaptor<RentalContract> captor = ArgumentCaptor.forClass(RentalContract.class);
        when(contractRepository.save(any(RentalContract.class))).thenAnswer(invocation -> {
            RentalContract c = invocation.getArgument(0);
            c.setId(123L);
            return c;
        });

        // Act
        RentalContractResponseDto response = service.createContract(registrationNumber, start, end);

        // Assert
        assertNotNull(response);
        assertEquals(clientId, response.getClientId());
        assertEquals(vehicle.getRegistrationNumber(), response.getVehicleId());
        assertEquals(start, response.getStartDate());
        assertEquals(end, response.getEndDate());
        assertEquals(operator.getId(), response.getOperatorId());

        verify(contractRepository).save(captor.capture());
        RentalContract saved = captor.getValue();
        assertEquals(vehicle, saved.getVehicle());
        assertEquals(client, saved.getUser());
        assertEquals(start, saved.getStartDate());
        assertEquals(end, saved.getEndDate());
        assertNotNull(saved.getCreatedAt());
        assertEquals(operator, saved.getOperator());
    }

    @Test
    void createContract_throwsWhenVehicleNotFound() {
        String registrationNumber = "DB99XDR";
        when(vehicleRepository.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.createContract(registrationNumber, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @Test
    void createContract_throwsWhenClientNotFound() {
        Long clientId = 1L;
        String registrationNumber = "DB95XDR";
        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(registrationNumber);

        when(vehicleRepository.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.of(vehicle));
//        when(userRepository.findById(clientId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> service.createContract(registrationNumber, LocalDate.now(), LocalDate.now().plusDays(1)));
    }

    @Test
    void createContract_throwsWhenConflictExists() {
        Long clientId = 1L;
        String registrationNumber = "DB95XDR";
        LocalDate start = LocalDate.now();
        LocalDate end = LocalDate.now().plusDays(2);

        Vehicle vehicle = new Vehicle();
        vehicle.setRegistrationNumber(registrationNumber);

        User client = new User();
        client.setId(clientId);

        when(vehicleRepository.findByRegistrationNumber(registrationNumber)).thenReturn(Optional.of(vehicle));
//        when(userRepository.findById(clientId)).thenReturn(Optional.of(client));
        when(contractRepository.existsByVehicleAndEndDateAfterAndStartDateBefore(eq(vehicle), eq(start), eq(end)))
                .thenReturn(true);

        assertThrows(IllegalStateException.class, () -> service.createContract(registrationNumber, start, end));
    }
}
