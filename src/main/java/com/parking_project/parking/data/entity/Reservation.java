package com.parking_project.parking.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
@NoArgsConstructor
@Setter
@Getter
public class Reservation {
    @Id
//    Not sure I need to set a particular column name for this one, but I'm doing it just in case
    @Column(name = "reservation_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "slot_id", nullable = false)
    private ParkingSlot parkingSlot;

    @ManyToOne
    @JoinColumn(name = "license_plate", nullable = false)
    private Car car;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "start_time", nullable = false)
    private Date startDate;

    @Column(name = "end_time", nullable = false)
    private Date endDate;

}
