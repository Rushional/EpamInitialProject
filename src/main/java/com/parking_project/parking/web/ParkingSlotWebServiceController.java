package com.parking_project.parking.web;

import com.parking_project.parking.business.service.BookingServiceImplementation;
import com.parking_project.parking.data.entity.ParkingSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/availableSlots")
public class ParkingSlotWebServiceController {
    private final BookingServiceImplementation bookingServiceImplementation;

    @Autowired
    public ParkingSlotWebServiceController(BookingServiceImplementation bookingServiceImplementation) {
        this.bookingServiceImplementation = bookingServiceImplementation;
    }
    @GetMapping
    public List<ParkingSlot> getAllSlots() {
        return this.bookingServiceImplementation.getAvailableSlots();
    }
}
