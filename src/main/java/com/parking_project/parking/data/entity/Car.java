package com.parking_project.parking.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "car")
@NoArgsConstructor
@Setter
@Getter
public class Car {
    @Id
    @Column(name = "license_plate", length = 16, nullable = false, unique = true)
    private String licensePlate;

    @ManyToMany(mappedBy = "cars", fetch = FetchType.LAZY)
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy="car")
    private List<Reservation> reservations = new ArrayList<>();

// I haven't added brand and model columns because I don't think we need them
}
