package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findReservationsByStartDateAndEndDate(Date startTime, Date endTime);
    List<Reservation> findReservationsByCustomerId(Long id);

}
