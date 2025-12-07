package com.example.maas.service;

import com.example.maas.entities.Towing;
import com.example.maas.entities.Vehicle;
import com.example.maas.repository.TowingRepository;
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
public class TowingServiceTest {

    @Mock
    private TowingRepository towingRepository;

    @InjectMocks
    private TowingService towingService;

    private Towing towing1;

    @BeforeEach
    void setUp() {
        Vehicle vehicle = new Vehicle();
        vehicle.setId(3L);

        towing1 = new Towing();
        towing1.setId(1L);
        towing1.setVehicle(vehicle);
        towing1.setDate(LocalDate.of(2023, 4, 5));
        towing1.setLocation("Highway 101");
        towing1.setReason("Engine failure");
        towing1.setDuration(2);
    }

    @Test
    void getTowingHistory_shouldReturnTowingList_forVehicle() {
        when(towingRepository.findByVehicleId(3L)).thenReturn(Arrays.asList(towing1));

        List<Towing> result = towingService.getTowingHistory(3L);

        assertEquals(1, result.size());
        assertEquals(towing1.getId(), result.get(0).getId());
    }
}
