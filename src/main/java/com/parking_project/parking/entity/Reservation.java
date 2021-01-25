package com.parking_project.parking.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Column(name = "start_time", nullable = false)
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    @Column(name = "end_time", nullable = false)
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }
}
