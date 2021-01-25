package com.parking_project.parking.entity;


import javax.persistence.*;

@Entity
@Table(name = "PARKING_PLACE")
public class ParkingSlot {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "DESCRIPTION", length = 2048, nullable = true, unique = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS")
    private StatusType status;

    public String getDescription() {
        return description;
    }

    public StatusType getStatus() {
        return status;
    }
}
