package com.parking_project.parking.data.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "parking_slot")
@NoArgsConstructor
@Setter
@Getter
public class ParkingSlot {
    @Id
    @Column(name = "slot_id")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

    @Column(name = "description", length = 2048, nullable = true, unique = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusType status;

//recursion problem
//    @OneToMany(mappedBy="parkingSlot")
//    private List<Reservation> reservations = new ArrayList<>();
}
