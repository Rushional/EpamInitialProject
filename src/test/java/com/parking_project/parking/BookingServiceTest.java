package com.parking_project.parking;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
public class BookingServiceTest {
//    @TestConfiguration
//    static class BookingServiceImplTestContextConfiguration {
//
//        @Bean
//        public BookingService bookingService() {
////            aaaand our services aren't interfaces but actual classes, so I'd need to refactor them into interfaces
////            to make this work. And I'm not sure that change would not break anything, so I'll try another route.
////            First I'll try to use Mockito instead of SpringExtension
//            return new BookingServiceImplementation();
//        }
//    }
//
//    @Autowired
//    private BookingService bookingService;
//
//    @MockBean
//    private ParkingSlotRepository parkingSlotRepository;
//
//    @Test
//    public void whenGetAvailableSlots_thenUnavailableShouldNotBePresent() {
//        ParkingSlot testAvailableSlot = new ParkingSlot();
//        testAvailableSlot.setId(54L);
//        testAvailableSlot.setStatus(StatusType.FREE);
//
//        ParkingSlot testOccupiedSlot = new ParkingSlot();
//        testOccupiedSlot.setId(55L);
//        testOccupiedSlot.setStatus(StatusType.OCCUPIED);
//
//        List<ParkingSlot> parkingSlotsList = new ArrayList<>();
//        parkingSlotsList.add(testAvailableSlot);
//
//        Mockito.when(parkingSlotRepository.findParkingSlotsByStatus(StatusType.FREE))
//                .thenReturn(parkingSlotsList);
//
//        List<ParkingSlot> found = bookingService.getAvailableSlots();
////        Make sure that the mocked object's method was called correctly:
//        verify(parkingSlotRepository, times(1)).findParkingSlotsByStatus(StatusType.FREE);
////        times(1) is the default value in verify(...),
////        but I wrote it anyway. I'm inexperienced with Mockito, so I'm not ready to omit default values
//
////        Make sure that the resulting list has the correct size (which means testOccupiedSlot isn't added to the list)
//        assertEquals(1, found.size());
//        assertThat(found.get(0))
//                .isEqualTo(testAvailableSlot);
//    }

//    @Test
//    public void whenGetAvailableSlots_thenAllAvailableShouldBePresent() {
//        fail();
//    }
//
//    @Test
//    public void whenNoAvailableSlots_getAvailableSlotsShouldReturnEmptyList() {
//        fail();
//    }
}
