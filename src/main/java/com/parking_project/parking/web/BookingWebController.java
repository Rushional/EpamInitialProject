package com.parking_project.parking.web;

import com.parking_project.parking.business.domain.SlotReservation;
import com.parking_project.parking.business.service.BookingService;
import com.parking_project.parking.business.service.CarService;
import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.business.service.ParkingSlotService;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("api/booking")
public class BookingWebController {

    private final CarService carService;
    private final CustomerService customerService;
    private final ParkingSlotService parkingSlotService;

    private final BookingService bookingService;

    @Autowired
    public BookingWebController(CarService carService, CustomerService customerService, ParkingSlotService parkingSlotService, BookingService bookingService) {
        this.carService = carService;
        this.customerService = customerService;
        this.parkingSlotService = parkingSlotService;
        this.bookingService = bookingService;
    }

    @GetMapping("{customer_id}")
    public ResponseEntity<List<Reservation>> getReservationsByCustomerAndId(@PathVariable("customer_id") String id) {
        Customer customer = customerService.getCustomerById(id);
        List<Reservation> reservationList = bookingService.getReservationsByCustomer(customer);
        return new ResponseEntity<List<Reservation>>(reservationList, HttpStatus.OK);
    }

    @GetMapping()
    public List<Reservation> getAllReservations() {
        return bookingService.getAllReservations();
    }

    @GetMapping("/date")
    public List<Reservation> getAvailableReservationsByDate(@RequestParam String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date);
        Date parseDate = Date.from(localDateTime.atZone(ZoneOffset.UTC).toInstant());
        List<Reservation> reservations = bookingService.getAvailableSlotsByDate(parseDate);

        return reservations;
    }

    @PostMapping
    public ResponseEntity<Void> createReservation(@RequestParam String slotId, @RequestParam String customerId,
                                                  @RequestParam String carId, @RequestParam String startDate,
                                                  @RequestParam String endDate) {
        bookingService.createReservation(slotId, customerId, carId, startDate, endDate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
