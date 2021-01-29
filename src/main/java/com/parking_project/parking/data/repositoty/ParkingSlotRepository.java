package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.ParkingSlot;
import com.parking_project.parking.data.entity.StatusType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot, Long> {
    Iterable<ParkingSlot> findParkingSlotsByStatus(StatusType statusType);
}
