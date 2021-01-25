package com.parking_project.parking.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "SLOT_REQUEST")
public class SlotRequest {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

//    TODO: one-to-one link to a parking slot

//    TODO: one-to-one link to a car

    @Column(name = "START_TIME", nullable = false)
    private Date startDate;

    public Date getStartDate() {
        return startDate;
    }

    @Column(name = "END_TIME", nullable = false)
    private Date endDate;

    public Date getEndDate() {
        return endDate;
    }
}
