package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByStartDateGreaterThanEqualAndEndDateLessThanEqual(Date startTime, Date endTime);
    List<Reservation> findReservationsByCustomerId(Long id);
    List<Reservation> findReservationsByParkingSlotAndStartDateGreaterThanEqual(ParkingSlot parkingSlot, Date date);
    List<Reservation> findReservationsByStartDateGreaterThanEqualAndStartDateLessThanEqual(Date startTime, Date endTime);

}
