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

    public List<SlotReservation> getAvailableSlotsByDate(Date date) {

        Iterable<ParkingSlot> slots = this.parkingSlotRepository.findAll();
        Map<Long, List<SlotReservation>> reservationSlotsMap = new HashMap<>();
        slots.forEach(slot -> {
            if (slot.getStatus().equals(StatusType.ACTIVE)) {
                reservationSlotsMap.put(slot.getId(), new ArrayList<>());
            }
        });
        fillReservationsMapByDate(reservationSlotsMap, date);
        List<SlotReservation> reservations = new ArrayList<>();
        for (List<SlotReservation> list : reservationSlotsMap.values()) {
            reservations.addAll(list);
        }
        return reservations;
    }


    private void fillReservationsMapByDate(Map<Long, List<SlotReservation>> reservationSlotsMap, Date date) {
        Iterable<Reservation> reservations = getReservationsByDate(date);
        Iterator<Reservation> iterator = reservations.iterator();
        while (iterator.hasNext()) {
            Reservation reservation = iterator.next();
            if (!isAvailableTime(reservation, date)) {
                continue;
            }
            Date durationEnd = findNextReservationDate(reservations, reservation, date);

            if ((!reservation.getEndDate().equals(durationEnd)) && reservation.getEndDate().before(durationEnd)) {
                SlotReservation slotReservation = new SlotReservation();
                slotReservation.setSlotId(reservation.getParkingSlot().getId());
                slotReservation.setStart(reservation.getEndDate());
                slotReservation.setEnd(durationEnd);

                List<SlotReservation> slotReservations = reservationSlotsMap.get(reservation.getParkingSlot().getId());
                slotReservations.add(slotReservation);
            }
        }
        fillFirstReservationDates(reservations, reservationSlotsMap, getStartOfDay(date));
        fillEndReservationsDate(reservationSlotsMap, date);

    }

    private boolean isAvailableTime(Reservation reservation, Date date) {
        Instant instant = date.toInstant();
        Date nextDay = Date.from(instant.plus(Period.ofDays(1)));
        Date resStart = Date.from(reservation.getStartDate().toInstant());
        Date resEnd = Date.from(reservation.getEndDate().toInstant());
        Date start = getStartOfDay(date);
        Date end = getEndOfDay(date);

        if (resStart.equals(resEnd)) {
            return false;
        }
        if ((resStart.equals(start) || resStart.before(start)) && (resEnd.equals(end) || resEnd.after(end))) {
            return false;
        }
        if (resEnd.before(start)) {
            return false;
        }
        if (resStart.after(nextDay)) {
            return false;
        }
        return true;
    }

    private void fillEndReservationsDate(Map<Long, List<SlotReservation>> reservations, Date date) {
        Date endDate = getEndOfDay(date);
        for (List<SlotReservation> list : reservations.values()) {
            list.forEach(slotReservation -> {
                if (null == slotReservation.getEnd()) {
                    slotReservation.setEnd(endDate);
                }
                if (slotReservation.getEnd().after(getEndOfDay(date))) {
                    slotReservation.setEnd(getEndOfDay(date));
                }
            });
        }
    }

    public Date findNextReservationDate(Iterable<Reservation> reservations, Reservation reservation, Date date) {
        Date firstEndTime = Date.from(reservation.getEndDate().toInstant());
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

    public void fillFirstReservationDates(Iterable<Reservation> reservations, Map<Long, List<SlotReservation>> reservationsMap, Date day) {
        Date date = Date.from(day.toInstant());
        for (Long slotId : reservationsMap.keySet()) {
            Date first = null;
            for (Reservation reservation : reservations) {
                if (reservation.getParkingSlot().getId().equals(slotId)) {
                    if (null == first && reservation.getEndDate().after(date)) {
                        first = Date.from(reservation.getStartDate().toInstant());
                    }
                    if (null != first && first.after(reservation.getStartDate())) {
                        first = Date.from(reservation.getStartDate().toInstant());
                    }
                }
            }

            SlotReservation slotReservation = new SlotReservation();
            slotReservation.setSlotId(slotId);

            if (first == null) {
                first = getStartOfDay(date);
                slotReservation.setStart(first);
            } else {
                if (first.equals(getStartOfDay(date))) {
                    continue;
                }
                slotReservation.setStart(getStartOfDay(date));
                slotReservation.setEnd(first);
            }
            reservationsMap.get(slotId).add(slotReservation);
        }
    }

    public List<Reservation> getReservationsByCustomer(Customer customer) {
        return reservationRepository.findReservationsByCustomerId(customer.getId());
    }

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Iterable<Reservation> getReservationsByDate(Date date) {
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime();
        Instant instant = localDateTime.atZone(ZoneOffset.systemDefault()).toInstant();
        Instant startInstant = instant.minus(Duration.ofHours(24));
        Instant endInstant = instant.plus(Duration.ofHours(48));

        Date startDate = Date.from(startInstant);
        Date endDate = Date.from(endInstant);

        java.sql.Date sqlStart = new java.sql.Date(startDate.getTime());
        java.sql.Date sqlEnd = new java.sql.Date(endDate.getTime());
        return reservationRepository.findReservationsByStartDateGreaterThanEqualAndEndDateLessThanEqual(sqlStart, sqlEnd);
    }


    private Date getEndOfDay(Date date) {
        LocalDateTime now = date.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime();
        LocalDateTime endOfDay = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 23, 59, 59);
        Date resultDate = Date.from(endOfDay.atZone(ZoneId.systemDefault()).toInstant());
        return resultDate;
    }

    private Date getStartOfDay(Date date) {
        LocalDateTime now = date.toInstant().atZone(ZoneOffset.systemDefault()).toLocalDateTime();
        LocalDateTime startOfDate = LocalDateTime
                .of(now.getYear(), now.getMonth(), now.getDayOfMonth(), 0, 0, 0);
        Date resultDate = Date.from(startOfDate.atZone(ZoneId.systemDefault()).toInstant());
        return resultDate;
    }

    public boolean createReservation(String slotId, String customerId, String carId, String startDate, String endDate) {
        LocalDateTime startLocalDateTime = LocalDateTime.parse(startDate);
        Date start = Date.from(startLocalDateTime.atZone(ZoneOffset.UTC).toInstant());
        LocalDateTime endLocalDateTime = LocalDateTime.parse(endDate);
        Date end = Date.from(endLocalDateTime.atZone(ZoneOffset.UTC).toInstant());

        if (start.equals(end) || start.after(end) || startLocalDateTime.plusDays(1).isBefore(endLocalDateTime)) {
            return false;
        }

        if (!isFreeSlot(slotId, start, end)) {
            return false;
        }

        ParkingSlot parkingSlot = parkingSlotRepository.findById(Long.valueOf(slotId)).orElseThrow();
        Customer customer = customerRepository.findCustomerById(Long.valueOf(customerId));
        Car car = carRepository.findById(carId).orElseThrow();

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setCar(car);
        reservation.setEndDate(end);
        reservation.setStartDate(start);
        reservation.setParkingSlot(parkingSlot);

        reservationRepository.save(reservation);
        reservationRepository.flush();
        return true;
    }

    private boolean isFreeSlot(String slotId, Date startDate, Date endDate) {
        ParkingSlot parkingSlot = parkingSlotRepository.findById(Long.parseLong(slotId)).orElseThrow();
        if (!parkingSlot.getStatus().equals(StatusType.ACTIVE)) {
            return false;
        }

        LocalDateTime dateTimeStart = startDate.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDate startDay = LocalDate.of(dateTimeStart.getYear(), dateTimeStart.getMonth(), dateTimeStart.getDayOfMonth());
        LocalDateTime endTimeStart =endDate.toInstant().atZone(ZoneOffset.UTC).toLocalDateTime();
        LocalDate endDay = LocalDate.of(endTimeStart.getYear(), endTimeStart.getMonth(), endTimeStart.getDayOfMonth());

        Set<SlotReservation> available = new HashSet<>(getAvailableSlotsByDate(startDate));
        if (!startDay.equals(endDay)) {
            available.addAll(getAvailableSlotsByDate(endDate));
        }

        for (SlotReservation slotReservation : available) {
            if ((slotReservation.getStart().before(startDate) || slotReservation.getStart().equals(startDate))
                    && (slotReservation.getEnd().after(endDate)) || slotReservation.getEnd().equals(endDate)) {
                return true;
            }
        }
        return false;
    }

    public void removeReservationBySlotAfterDate(String slotId, Date startDate) {
        Date start = Date.from(startDate.toInstant());
        java.sql.Date sqlStart = new java.sql.Date(start.getTime());
        ParkingSlot parkingSlot = parkingSlotRepository.findById(Long.valueOf(slotId)).orElseThrow();
        List<Reservation> reservations = reservationRepository
                .findReservationsByParkingSlotAndStartDateGreaterThanEqual(parkingSlot, sqlStart);
        reservations.forEach(reservationRepository::delete);
    }
}
