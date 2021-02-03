package com.parking_project.parking.web;

import com.parking_project.parking.business.service.BookingService;
import com.parking_project.parking.data.entity.ParkingSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/availableSlots")
public class ParkingSlotWebServiceController {
    private final BookingService bookingService;

    @Autowired
    public ParkingSlotWebServiceController(BookingService bookingService) {
        this.bookingService = bookingService;
    }
    @GetMapping
    public List<ParkingSlot> getAllSlots() {
        return this.bookingService.getAvailableSlots();
    }
}
