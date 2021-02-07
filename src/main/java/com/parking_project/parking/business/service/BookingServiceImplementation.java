package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.StatusType;
import com.parking_project.parking.data.repositoty.ParkingSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookingServiceImplementation implements BookingService {
    private final ParkingSlotRepository parkingSlotRepository;

    @Autowired
    public BookingServiceImplementation(ParkingSlotRepository parkingSlotRepository) {
        this.parkingSlotRepository = parkingSlotRepository;
    }

    public List<ParkingSlot> getAllSlots() {
        Iterable<ParkingSlot> slots = parkingSlotRepository.findAll();
        List<ParkingSlot> slotsList = new ArrayList<>();
        slots.forEach(slotsList::add);
        slotsList.sort(new ParkingSlotsComparator());
        return slotsList;
    }

    public List<ParkingSlot> getAvailableSlots() {
        Iterable<ParkingSlot> slots = parkingSlotRepository.findParkingSlotsByStatus(StatusType.FREE);
        List<ParkingSlot> available = new ArrayList<>();
        slots.forEach(available::add);
        available.sort(new ParkingSlotsComparator());
        return available;
    }

    private class ParkingSlotsComparator implements Comparator<ParkingSlot> {
        @Override
        public int compare(ParkingSlot parkingSlot1, ParkingSlot parkingSlot2) {
            return parkingSlot1.getId().compareTo(parkingSlot2.getId());
        }
    }

}
