package com.example.maas.service;

import com.example.maas.entities.Maintenance;
import com.example.maas.entities.MaintenanceDto;
import com.example.maas.entities.Vehicle;
import com.example.maas.repository.MaintenanceRepository;
import com.example.maas.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MaintenanceServiceTest {

    @Mock
    private MaintenanceRepository maintenanceRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private MaintenanceService maintenanceService;

    private Maintenance maintenance1;
    private Maintenance maintenance2;

    @BeforeEach
    void setUp() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(1L);

        maintenance1 = new Maintenance();
        maintenance1.setId(1L);
        maintenance1.setVehicle(vehicle);
        maintenance1.setDate(LocalDate.of(2023, 1, 15));
        maintenance1.setType("Oil Change");
        maintenance1.setDescription("Regular oil change");
        maintenance1.setCost(50.00);

        maintenance2 = new Maintenance();
        maintenance2.setId(2L);
        maintenance2.setVehicle(vehicle);
        maintenance2.setDate(LocalDate.of(2023, 3, 20));
        maintenance2.setType("Tire Rotation");
        maintenance2.setDescription("Rotated all four tires");
        maintenance2.setCost(30.00);
    }

    @Test
    void getMaintenanceHistory_shouldReturnMaintenanceList_forVehicle() {
        when(maintenanceRepository.findByVehicleId(1L)).thenReturn(Arrays.asList(maintenance1, maintenance2));

        List<MaintenanceDto> result = maintenanceService.getMaintenanceHistory(1L);

        assertEquals(2, result.size());
        assertEquals(maintenance1.getId(), result.get(0).getId());
        assertEquals(maintenance2.getId(), result.get(1).getId());
    }
}
