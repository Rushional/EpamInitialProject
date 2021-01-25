package com.parking_project.parking.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "reservation")
public class Reservation {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

//    TODO: one-to-one link to a parking slot

//    TODO: one-to-one link to a car

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
