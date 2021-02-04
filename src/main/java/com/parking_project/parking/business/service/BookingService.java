package com.parking_project.parking.business.service;

import com.parking_project.parking.business.domain.SlotReservation;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.Reservation;
import com.parking_project.parking.data.entity.StatusType;
import com.parking_project.parking.data.repositoty.CarRepository;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import com.parking_project.parking.data.repositoty.ParkingSlotRepository;
import com.parking_project.parking.data.repositoty.ReservationRepository;
import org.hibernate.mapping.Collection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.datetime.DateFormatter;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.time.*;
import java.util.*;

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

    public List<Reservation> getAvailableSlotsByDate(Date date) {

        Iterable<ParkingSlot> slots = this.parkingSlotRepository.findAll();
        Map<Long, List<SlotReservation>> reservationSlotsMap = new HashMap<>();
        slots.forEach(slot -> {
            reservationSlotsMap.put(slot.getId(), new ArrayList<>());
        });

        fillReservationsMapByDate(reservationSlotsMap, date);
        List<Reservation> reservations = new ArrayList<>();
        for (List<SlotReservation> list : reservationSlotsMap.values()) {
            for (SlotReservation slotReservation : list) {
                Reservation reservation = new Reservation();
                reservation.setCar(carRepository.findById(slotReservation.getCar_id() + "").orElseThrow());
                reservation.setParkingSlot(parkingSlotRepository.findById(slotReservation.getSlotId()).orElseThrow());
                reservation.setCustomer(customerRepository.findCustomerById(slotReservation.getCustomer_id()));
                reservation.setStartDate(slotReservation.getStart());
                reservation.setEndDate(slotReservation.getEnd());
                reservations.add(reservation);
            }
        }
        return reservations;
    }


    private void fillReservationsMapByDate(Map<Long, List<SlotReservation>> reservationSlotsMap, Date date) {
        Iterable<Reservation> reservations = getReservationsByDate(date);
        Instant instant = date.toInstant();
        Date nextDay = Date.from(instant.plus(Period.ofDays(1)));

        while (reservations.iterator().hasNext()) {
            Reservation reservation = reservations.iterator().next();
            if (reservation.getEndDate().before(date) || reservation.getStartDate().after(nextDay)) {
                continue;
            }
            Date durationEnd = findNextReservationDate(reservations, reservation);
            if (durationEnd.equals(reservation.getEndDate())) {
                durationEnd = getEndOfDay(date);
            }
            if (!reservation.getEndDate().equals(durationEnd)) {
                SlotReservation slotReservation = new SlotReservation();
                slotReservation.setSlotId(reservation.getParkingSlot().getId());
                slotReservation.setDescription(reservation.getParkingSlot().getDescription());
                slotReservation.setStart(reservation.getEndDate());
                slotReservation.setEnd(durationEnd);

                List<SlotReservation> currentSlot = reservationSlotsMap.get(reservation.getParkingSlot().getId());
                currentSlot.add(slotReservation);
            }
        }
        fillFirstReservationDates(reservations, reservationSlotsMap, getStartOfDay(date));

    }

    public Date findNextReservationDate(Iterable<Reservation> reservations, Reservation reservation) {
        Date firstEndTime = reservation.getEndDate();
        for (Reservation currentReservation : reservations) {
            if (!currentReservation.getParkingSlot().equals(reservation.getParkingSlot())) {
                continue;
            }
            if (firstEndTime == reservation.getEndDate()) {
                if (currentReservation.getStartDate().after(firstEndTime)) {
                    firstEndTime = currentReservation.getStartDate();
                }
            } else {
                if (firstEndTime.after(currentReservation.getStartDate())) {
                    firstEndTime = currentReservation.getStartDate();
                }
            }
        }
        return firstEndTime;
    }

    public void fillFirstReservationDates(Iterable<Reservation> reservations, Map<Long, List<SlotReservation>> reservationsMap, Date date) {
        for (Long slotId : reservationsMap.keySet()) {
            Date first = null;
            for (Reservation reservation : reservations) {
                if (reservation.getParkingSlot().getId().equals(slotId)) {
                    if (null == first && reservation.getEndDate().after(date)) {
                        first = reservation.getStartDate();
                    } else {
                        continue;
                    }
                    if (first.after(reservation.getStartDate())) {
                        first = reservation.getStartDate();
                    }
                }
            }
            if (first == null) {
                first = getStartOfDay(date);
            }
            if (first.after(date)) {
                SlotReservation slotReservation = new SlotReservation();
                slotReservation.setStart(date);
                slotReservation.setEnd(first);
                slotReservation.setSlotId(slotId);
                reservationsMap.get(slotId).add(slotReservation);
            }
        }

    }

    public List<Reservation> getReservationsByCustomer(Customer customer) {
        return reservationRepository.findReservationsByCustomerId(customer.getId());
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Iterable<Reservation> getReservationsByDate(Date date) {
        Instant instant = date.toInstant();
        Instant startInstant = instant.minus(Duration.ofHours(24));
        Instant endInstant = instant.plus(Duration.ofHours(48));

        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        java.sql.Date sqlStart = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(endDate.getTime());
        return this.reservationRepository.findReservationsByStartDateAndEndDate(sqlStart, sqlEnd);
    }


    private Date getEndOfDay(Date date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        return Date.from(endOfDay.toInstant(ZoneOffset.UTC));
    }

    private Date getStartOfDay(Date date) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime endOfDay = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        return Date.from(endOfDay.toInstant(ZoneOffset.UTC));
    }

}
