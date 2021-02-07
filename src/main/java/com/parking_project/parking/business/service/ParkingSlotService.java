package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.StatusType;
import com.parking_project.parking.data.repositoty.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class ParkingSlotService {
    private final ParkingSlotRepository parkingSlotRepository;
    private final BookingService bookingService;

    @Autowired
    public ParkingSlotService(ParkingSlotRepository parkingSlotRepository, BookingService bookingService) {
        this.parkingSlotRepository = parkingSlotRepository;
        this.bookingService = bookingService;
    }

    public List<ParkingSlot> getAllSlots() {
        Iterable<ParkingSlot> slots = this.parkingSlotRepository.findAll();
        List<ParkingSlot> slotsList = new ArrayList<>();
        slots.forEach(slotsList::add);
        slotsList.sort(new Comparator<ParkingSlot>() {
            @Override
            public int compare(ParkingSlot parkingSlot1, ParkingSlot parkingSlot2) {
                return parkingSlot1.getId().compareTo(parkingSlot2.getId());
            }
        });

        return slotsList;
    }

    public ParkingSlot getSlotById(Long id) {
       return parkingSlotRepository.findById(id)
               .orElseThrow(() -> new RuntimeException("Slot is missing from the database"));
    }

    public void addParkingSlot(String description) {
        ParkingSlot parkingSlot = new ParkingSlot();
        parkingSlot.setDescription(description);
        parkingSlot.setStatus(StatusType.ACTIVE);
        parkingSlotRepository.save(parkingSlot);
        parkingSlotRepository.flush();
    }

    public void removeParkingSlot(String id) {
        ParkingSlot tmpParkingSlot = getSlotById(Long.valueOf(id));
        tmpParkingSlot.setStatus(StatusType.DISABLED);
        bookingService.removeReservationBySlotAfterDate(id, Date.from(Instant.now()));
        parkingSlotRepository.save(tmpParkingSlot);
        parkingSlotRepository.flush();
    }
}
