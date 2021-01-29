package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.StatusType;
import com.parking_project.parking.data.repositoty.CarRepository;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import com.parking_project.parking.data.repositoty.ParkingSlotRepository;
import com.parking_project.parking.data.repositoty.ReservationRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class BookingService {
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final ParkingSlotRepository parkingSlotRepository;
    private final ReservationRepository reservationRepository;

    @Autowired
    public BookingService(CarRepository carRepository, CustomerRepository customerRepository,
                          ParkingSlotRepository parkingSlotRepository, ReservationRepository reservationRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.parkingSlotRepository = parkingSlotRepository;
        this.reservationRepository = reservationRepository;
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

    public List<ParkingSlot> getAvailableSlots() {
        Iterable<ParkingSlot> slots = this.parkingSlotRepository.findParkingSlotsByStatus(StatusType.FREE);
        List<ParkingSlot> available = new ArrayList<>();
        slots.forEach(available::add);

        available.sort(new Comparator<ParkingSlot>() {
            @Override
            public int compare(ParkingSlot parkingSlot1, ParkingSlot parkingSlot2) {
                return parkingSlot1.getId().compareTo(parkingSlot2.getId());
            }
        });
        return available;
    }

}
