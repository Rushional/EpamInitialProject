package com.parking_project.parking.web;

import com.parking_project.parking.business.domain.SlotReservation;
import com.parking_project.parking.business.service.BookingService;
import com.parking_project.parking.business.service.ParkingSlotService;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.Reservation;
import com.parking_project.parking.data.entity.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/parking")
public class ParkingSlotWebServiceController {
    private final ParkingSlotService parkingSlotService;
    private final BookingService bookingService;

    @Autowired
    public ParkingSlotWebServiceController(ParkingSlotService parkingSlotService, BookingService bookingService) {
        this.parkingSlotService = parkingSlotService;
        this.bookingService = bookingService;
    }

    @GetMapping("availableSlots")
    public List<Reservation> getAvailableSlotsForNow() {
        return this.bookingService.getAvailableSlotsByDate(Date.from(Instant.now()));
    }

    @GetMapping()
    public List<ParkingSlot> getAllSlots() {
        return this.parkingSlotService.getAllSlots();
    }

    @PostMapping("addParking")
    public ResponseEntity<Void> addParkingSlot(@RequestParam String description, UriComponentsBuilder builder) {

        parkingSlotService.addParkingSlot(description);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeParkingSlotById(@PathVariable("id") String id) {
        parkingSlotService.removeParkingSlot(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }


}
