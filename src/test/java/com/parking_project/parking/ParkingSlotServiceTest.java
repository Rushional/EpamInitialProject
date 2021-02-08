package com.parking_project.parking;

import com.parking_project.parking.business.service.BookingService;
import com.parking_project.parking.business.service.ParkingSlotService;
import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.StatusType;
import com.parking_project.parking.data.repositoty.ParkingSlotRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

//Looks like I only need Mockito at this point
//@ExtendWith(SpringExtension.class)
@ExtendWith(MockitoExtension.class)
public class ParkingSlotServiceTest {
    @Mock
    ParkingSlotRepository parkingSlotRepository;

    @Mock
    BookingService bookingService;

//    I could make a @BeforeEach method that mocks the repositories,
//    But I'm not sure the same mocked class would suffice.
//    So for now I'm going with separate mocking.
//    This might be sub-optimal later on

    @Test
    public void getAllSlotsTest() {
        // Given
        ParkingSlot activeSlotA = new ParkingSlot();
        activeSlotA.setId(54L);
        activeSlotA.setStatus(StatusType.ACTIVE);
        activeSlotA.setDescription("Just your average active slot!");

        ParkingSlot activeSlotB = new ParkingSlot();
        activeSlotB.setId(55L);
        activeSlotB.setStatus(StatusType.ACTIVE);
        activeSlotB.setDescription("Active slot that came a bit too late to be your average one:c");

        ParkingSlot disabledSlot = new ParkingSlot();
        disabledSlot.setId(27373L);
        disabledSlot.setStatus(StatusType.DISABLED);
        disabledSlot.setDescription("Obliterated by a fallen tree. Ruin has come to our parking slot.");

        List<ParkingSlot> slotsList = new ArrayList<>();
        slotsList.add(activeSlotA);
        slotsList.add(activeSlotB);
        slotsList.add(disabledSlot);

        when(parkingSlotRepository.findAll()).thenReturn(slotsList);

        // When
        ParkingSlotService parkingSlotService = new ParkingSlotService(parkingSlotRepository, bookingService);
        List<ParkingSlot> resultList = parkingSlotService.getAllSlots();

        // Then

//        Make sure the correct method is called
        verify(parkingSlotRepository).findAll();
        assertEquals(3, resultList.size());
        assertTrue(resultList.contains(activeSlotA));
        assertTrue(resultList.contains(activeSlotB));
        assertTrue(resultList.contains(disabledSlot));
    }

    @Test
    public void whenGetSlotByWrongId_thenShouldThrowRuntimeException() {
        // Given
        Optional<ParkingSlot> emptyOptional = empty();
        when(parkingSlotRepository.findById(7654321L)).thenReturn(emptyOptional);

        // ummm When Then?.. how do you even?..

//        TODO:
//         This should probably be done in @BeforeAll, but MAKE IT WORK MAKE IT RIGHT
//         Don't want to be too optimal, eh?
        ParkingSlotService parkingSlotService = new ParkingSlotService(parkingSlotRepository, bookingService);
//        ParkingSlot found = parkingSlotService.getSlotById(7654321L);
        assertThrows(
                RuntimeException.class,
                ()-> parkingSlotService.getSlotById(7654321L),
                "Slot is missing from the database"
        );
    }

    @Test
    public void whenGetSlotById_thenReturnsCorrectSlot() {
        // Given
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setDescription("my of identifying the slot"); //probably superfluous but whatever
        Optional<ParkingSlot> filledOptional = Optional.of(parkingSlot);
        when(parkingSlotRepository.findById(7654321L)).thenReturn(filledOptional);

        // When
        ParkingSlotService parkingSlotService = new ParkingSlotService(parkingSlotRepository, bookingService);
        ParkingSlot found = parkingSlotService.getSlotById(7654321L);

        // Then
        assertEquals(parkingSlot, found);
    }
}
