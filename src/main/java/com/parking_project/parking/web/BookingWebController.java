package com.parking_project.parking.web;

import com.parking_project.parking.business.domain.SlotReservation;
import com.parking_project.parking.business.service.BookingService;
import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.Reservation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BookingWebController {
    private final CustomerService customerService;
    private final BookingService bookingService;

    @Autowired
    public BookingWebController(CustomerService customerService, BookingService bookingService) {
        this.customerService = customerService;
        this.bookingService = bookingService;
    }

    @GetMapping("/customer/{customer_id}/booking")
    public ResponseEntity<List<Reservation>> getReservationsByCustomerAndId(@PathVariable("customer_id") String id) {
        Customer customer = customerService.getCustomerById(id);
        List<Reservation> reservationList = bookingService.getReservationsByCustomer(customer);
        return new ResponseEntity<List<Reservation>>(reservationList, HttpStatus.OK);
    }

    @GetMapping("/reservations")
    public List<Reservation> getAllReservations() {
        return bookingService.getAllReservations();
    }

    @GetMapping("/parking/slots/available/date")
    public List<SlotReservation> getAvailableReservationsByDate(@RequestParam String date) {
        LocalDateTime localDateTime = LocalDateTime.parse(date).atZone(ZoneOffset.systemDefault()).toLocalDateTime();
        Date parseDate = Date.from(localDateTime.atZone(ZoneOffset.systemDefault()).toInstant());
        List<SlotReservation> reservations = bookingService.getAvailableSlotsByDate(parseDate);

        return reservations;
    }
    @GetMapping("/parking/slots/available")
    public List<SlotReservation> getAvailableSlotsForNow() {
        return this.bookingService.getAvailableSlotsByDate(Date.from(Instant.now()));
    }

    @PostMapping("/reservation/create")
    public ResponseEntity<Void> createReservation(@RequestParam String slotId, @RequestParam String customerId,
                                                  @RequestParam String carId, @RequestParam String startDate,
                                                  @RequestParam String endDate) {

        return  bookingService.createReservation(slotId, customerId, carId, startDate, endDate)
                ? new ResponseEntity<>(HttpStatus.CREATED)
                : new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
