package com.parking_project.parking.business.service;

import com.parking_project.parking.business.domain.SlotReservation;
import com.parking_project.parking.data.entity.*;
import com.parking_project.parking.data.repositoty.CarRepository;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import com.parking_project.parking.data.repositoty.ParkingSlotRepository;
import com.parking_project.parking.data.repositoty.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
                reservation.setParkingSlot(parkingSlotRepository.findById(slotReservation.getSlotId()).orElseThrow());
                reservation.setStartDate(slotReservation.getStart());
                reservation.setEndDate(slotReservation.getEnd());
                reservations.add(reservation);
            }
        }
        return reservations;
    }


    private void fillReservationsMapByDate(Map<Long, List<SlotReservation>> reservationSlotsMap, Date date) {
        Iterable<Reservation> reservations = getReservationsByDate(date);
        System.out.println(reservations);
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        Date nextDay = Date.from(instant.plus(Period.ofDays(1)));
        Iterator<Reservation> iterator = reservations.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (reservation.getEndDate().before(date) || reservation.getStartDate().after(nextDay)) {
                continue;
            }
            Date durationEnd = findNextReservationDate(reservations, reservation, date);

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
        fillEndReservationsDate(reservationSlotsMap, date);

    }

    private void fillEndReservationsDate(Map<Long, List<SlotReservation>> reservations, Date date) {
        System.out.println("fillEndReservationDate");
        Date endDate = getEndOfDay(date);
        for (List<SlotReservation> list : reservations.values()) {
            list.forEach(slotReservation -> {
                if (null == slotReservation.getEnd()) {
                    slotReservation.setEnd(endDate);
                }
            });
        }
    }

    public Date findNextReservationDate(Iterable<Reservation> reservations, Reservation reservation, Date date) {
        System.out.println("fillNextReservationDates");
        Date firstEndTime = reservation.getEndDate();
        for (Reservation currentReservation : reservations) {
            if (!currentReservation.getParkingSlot().equals(reservation.getParkingSlot())) {
                continue;
            }
            if (firstEndTime.equals(currentReservation.getEndDate())) {
                continue;
            }

            if (firstEndTime.before(currentReservation.getStartDate())) {
                firstEndTime = currentReservation.getStartDate();
            }
        }
        if (firstEndTime.equals(reservation.getEndDate())) {
            firstEndTime = getEndOfDay(date);
        }
        return firstEndTime;
    }

    public void fillFirstReservationDates(Iterable<Reservation> reservations, Map<Long, List<SlotReservation>> reservationsMap, Date date) {
        System.out.println("fillFirstReservationDates");
        for (Long slotId : reservationsMap.keySet()) {
            Date first = null;
            for (Reservation reservation : reservations) {
                if (reservation.getParkingSlot().getId().equals(slotId)) {
                    if (null == first && reservation.getEndDate().after(date)) {
                        first = reservation.getStartDate();
                    }
                    if (first.after(reservation.getStartDate())) {
                        first = reservation.getStartDate();
                    }
                }
            }

            SlotReservation slotReservation = new SlotReservation();
            slotReservation.setSlotId(slotId);

            if (first == null) {
                first = getStartOfDay(date);
                slotReservation.setStart(first);
            } else {
                slotReservation.setStart(getStartOfDay(date));
                slotReservation.setEnd(first);
            }
            reservationsMap.get(slotId).add(slotReservation);
        }
    }

    public List<Reservation> getReservationsByCustomer(Customer customer) {
        System.out.println("getReservationsByCustomer");
        return reservationRepository.findReservationsByCustomerId(customer.getId());
    }

    public List<Reservation> getAllReservations() {
        System.out.println("getAllreservations");
        return reservationRepository.findAll();
    }

    public Iterable<Reservation> getReservationsByDate(Date date) {
        System.out.println("getReservationsByDate");
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        Instant instant = localDateTime.toInstant(ZoneOffset.UTC);
        Instant startInstant = instant.minus(Duration.ofHours(24));
        Instant endInstant = instant.plus(Duration.ofHours(48));

        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        java.sql.Date sqlStart = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(endDate.getTime());
        return this.reservationRepository.findReservationsByStartDateGreaterThanEqualAndEndDateLessThanEqual(sqlStart, sqlEnd);
    }


    private Date getEndOfDay(Date date) {
        LocalDateTime now = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime endOfDay = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        Date resultDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return resultDate;
    }

    private Date getStartOfDay(Date date) {
        LocalDateTime now = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime startOfDate = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        Date resultDate = Date.from(startOfDate.atZone(ZoneId.systemDefault()).toInstant());
        return resultDate;
    }

    public void createReservation(String slotId, String customerId, String carId, String startDate, String endDate) {
        System.out.println("createReservation");
        ParkingSlot parkingSlot = parkingSlotRepository.findById(Long.valueOf(slotId)).orElseThrow();
        Customer customer = customerRepository.findCustomerById(Long.valueOf(customerId));
        Car car = carRepository.findById(carId).orElseThrow();
        LocalDateTime startLocalDateTime = LocalDateTime.parse(startDate);
        Date start = Date.from(startLocalDateTime.atZone(ZoneOffset.UTC).toInstant());
        LocalDateTime endLocalDateTime = LocalDateTime.parse(endDate);
        Date end = Date.from(endLocalDateTime.atZone(ZoneOffset.UTC).toInstant());

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setCar(car);
        reservation.setEndDate(end);
        reservation.setStartDate(start);
        reservation.setParkingSlot(parkingSlot);

        reservationRepository.save(reservation);
        reservationRepository.flush();
    }
}
