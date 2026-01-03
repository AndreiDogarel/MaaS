package com.example.maas.service;

import com.example.maas.entities.*;
import com.example.maas.repository.RentalRepository;
import com.example.maas.repository.UserRepository;
import com.example.maas.repository.VehicleRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalServiceTest {

    @Mock
    private RentalRepository rentalRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private HasValidDocuments hasValidDocuments;

    @InjectMocks
    private RentalService rentalService;

    @Captor
    private ArgumentCaptor<Rental> rentalCaptor;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void getRentalHistory_returnsListFromRepo() {
        Long vehicleId = 7L;
        Rental r1 = Rental.builder().id(1L).build();
        Rental r2 = Rental.builder().id(2L).build();

        when(rentalRepository.findByVehicleIdOrderByStartDateDesc(vehicleId)).thenReturn(List.of(r1, r2));

        List<Rental> out = rentalService.getRentalHistory(vehicleId);

        assertEquals(2, out.size());
        assertEquals(1L, out.get(0).getId());
        assertEquals(2L, out.get(1).getId());
        verify(rentalRepository).findByVehicleIdOrderByStartDateDesc(vehicleId);
    }

    @Test
    void getRentalById_delegatesToRepo() {
        when(rentalRepository.findById(10L)).thenReturn(Optional.of(Rental.builder().id(10L).build()));

        Optional<Rental> out = rentalService.getRentalById(10L);

        assertTrue(out.isPresent());
        assertEquals(10L, out.get().getId());
        verify(rentalRepository).findById(10L);
    }

    @Test
    void createRental_nullDto_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> rentalService.createRental(1L, null));
        verifyNoInteractions(vehicleRepository, userRepository, rentalRepository, hasValidDocuments);
    }

    @Test
    void createRental_vehicleNotFound_throwsIllegalArgumentException() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.empty());

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .build();

        assertThrows(IllegalArgumentException.class, () -> rentalService.createRental(1L, dto));
        verify(vehicleRepository).findById(1L);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    void createRental_notAuthenticated_throwsAccessDenied() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(new Vehicle()));

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(context);

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .build();

        assertThrows(AccessDeniedException.class, () -> rentalService.createRental(1L, dto));
        verify(vehicleRepository).findById(1L);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    void createRental_anonymousUser_throwsAccessDenied() {
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(new Vehicle()));

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("anonymousUser");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .build();

        assertThrows(AccessDeniedException.class, () -> rentalService.createRental(1L, dto));
        verify(vehicleRepository).findById(1L);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    void createRental_customerWithoutDocuments_throwsAccessDenied() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Roles customerRole = Roles.builder().name(Role.CUSTOMER).build();

        User principalUser = new User();
        principalUser.setId(99L);
        principalUser.setRole(customerRole);

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(principalUser);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(hasValidDocuments.check(99L)).thenReturn(false);

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .build();

        assertThrows(AccessDeniedException.class, () -> rentalService.createRental(1L, dto));
        verify(hasValidDocuments).check(99L);
        verifyNoInteractions(rentalRepository);
    }

    @Test
    void createRental_customerWithDocuments_persistsAndReturnsDto() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Roles customerRole = Roles.builder().name(Role.CUSTOMER).build();

        User principalUser = new User();
        principalUser.setId(99L);
        principalUser.setRole(customerRole);

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(principalUser);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(hasValidDocuments.check(99L)).thenReturn(true);

        Rental saved = Rental.builder()
                .id(123L)
                .vehicle(vehicle)
                .user(principalUser)
                .startDate(LocalDate.of(2026, 1, 3))
                .endDate(LocalDate.of(2026, 1, 5))
                .status(RentalStatus.COMPLETED)
                .odometerStart(100L)
                .odometerEnd(200L)
                .totalPrice(new BigDecimal("150.50"))
                .createdAt(LocalDateTime.of(2026, 1, 3, 12, 0))
                .build();

        when(rentalRepository.save(any(Rental.class))).thenReturn(saved);

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .endDate(LocalDate.of(2026, 1, 5))
                .status(RentalStatus.COMPLETED)
                .odometerStart(100L)
                .odometerEnd(200L)
                .totalPrice(new BigDecimal("150.50"))
                .build();

        Rental out = rentalService.createRental(1L, dto);

        assertNotNull(out);
        assertEquals(123L, out.getId());
        assertEquals(1L, out.getVehicle().getId());
        assertEquals(99L, out.getUser().getId());
        assertEquals(RentalStatus.COMPLETED, out.getStatus());
        verify(hasValidDocuments).check(99L);
        verify(rentalRepository).save(rentalCaptor.capture());

        Rental toSave = rentalCaptor.getValue();
        assertEquals(vehicle.getId(), toSave.getVehicle().getId());
        assertEquals(principalUser.getId(), toSave.getUser().getId());
        assertEquals(dto.getStartDate(), toSave.getStartDate());
        assertEquals(dto.getEndDate(), toSave.getEndDate());
    }

    @Test
    void createRental_adminBypassesDocumentCheck() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Roles adminRole = Roles.builder().name(Role.ADMIN).build();

        User principalUser = new User();
        principalUser.setId(77L);
        principalUser.setRole(adminRole);

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn(principalUser);

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        Rental saved = Rental.builder()
                .id(10L)
                .vehicle(vehicle)
                .user(principalUser)
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .createdAt(LocalDateTime.of(2026, 1, 3, 0, 0))
                .build();

        when(rentalRepository.save(any(Rental.class))).thenReturn(saved);

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .build();

        RentalDto out = rentalService.createRental(1L, dto).toRentalDto();

        assertEquals(10L, out.getId());
        verify(hasValidDocuments, never()).check(anyLong());
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void createRental_principalNotUser_usesFindByUsername() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(vehicle));

        Roles customerRole = Roles.builder().name(Role.CUSTOMER).build();

        User dbUser = new User();
        dbUser.setId(55L);
        dbUser.setRole(customerRole);

        Authentication auth = mock(Authentication.class);
        when(auth.isAuthenticated()).thenReturn(true);
        when(auth.getPrincipal()).thenReturn("not-a-user");
        when(auth.getName()).thenReturn("user1");

        SecurityContext context = mock(SecurityContext.class);
        when(context.getAuthentication()).thenReturn(auth);
        SecurityContextHolder.setContext(context);

        when(userRepository.findByUsername("user1")).thenReturn(Optional.of(dbUser));
        when(hasValidDocuments.check(55L)).thenReturn(true);

        Rental saved = Rental.builder()
                .id(99L)
                .vehicle(vehicle)
                .user(dbUser)
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .createdAt(LocalDateTime.of(2026, 1, 3, 0, 0))
                .build();

        when(rentalRepository.save(any(Rental.class))).thenReturn(saved);

        RentalDto dto = RentalDto.builder()
                .startDate(LocalDate.of(2026, 1, 3))
                .status(RentalStatus.COMPLETED)
                .build();

        Rental out = rentalService.createRental(1L, dto);

        assertEquals(99L, out.getId());
        assertEquals(55L, out.getUser().getId());
        verify(userRepository).findByUsername("user1");
        verify(hasValidDocuments).check(55L);
        verify(rentalRepository).save(any(Rental.class));
    }

    @Test
    void updateRental_notFound_throwsIllegalArgumentException() {
        when(rentalRepository.findById(1L)).thenReturn(Optional.empty());

        RentalUpdateDto dto = RentalUpdateDto.builder().status(RentalStatus.COMPLETED).build();

        assertThrows(IllegalArgumentException.class, () -> rentalService.updateRental(1L, dto));
        verify(rentalRepository).findById(1L);
        verify(rentalRepository, never()).save(any());
    }

    @Test
    void updateRental_partialUpdate_persists() {
        Rental existing = Rental.builder()
                .id(1L)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(null)
                .status(RentalStatus.COMPLETED)
                .odometerStart(10L)
                .odometerEnd(null)
                .totalPrice(new BigDecimal("10.00"))
                .build();

        when(rentalRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(rentalRepository.save(any(Rental.class))).thenAnswer(inv -> inv.getArgument(0));

        RentalUpdateDto dto = RentalUpdateDto.builder()
                .endDate(LocalDate.of(2026, 1, 2))
                .status(RentalStatus.COMPLETED)
                .odometerEnd(20L)
                .build();

        Rental out = rentalService.updateRental(1L, dto);

        assertEquals(1L, out.getId());
        assertEquals(RentalStatus.COMPLETED, out.getStatus());
        assertEquals(LocalDate.of(2026, 1, 2), out.getEndDate());
        assertEquals(10L, out.getOdometerStart());
        assertEquals(20L, out.getOdometerEnd());
        assertEquals(new BigDecimal("10.00"), out.getTotalPrice());
        verify(rentalRepository).save(existing);
    }

    @Test
    void deleteRental_notFound_returnsFalse() {
        when(rentalRepository.existsById(1L)).thenReturn(false);

        boolean out = rentalService.deleteRental(1L);

        assertFalse(out);
        verify(rentalRepository).existsById(1L);
        verify(rentalRepository, never()).deleteById(anyLong());
    }

    @Test
    void deleteRental_found_deletesAndReturnsTrue() {
        when(rentalRepository.existsById(1L)).thenReturn(true);

        boolean out = rentalService.deleteRental(1L);

        assertTrue(out);
        verify(rentalRepository).existsById(1L);
        verify(rentalRepository).deleteById(1L);
    }
}
