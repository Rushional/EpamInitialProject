package com.parking_project.parking.data.entity;

// Not sure if that's the right import - I totally might be using wrong annotations
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
//    TODO: Will we have a problem with id auto generation?
//     I mean, it might want to generate ID's automatically, and we don't want that
//    Well, if it's easy to fix, we'll fix it.
//    If it's at least somewhat complicated to fix, it's better to just add a column of numerical id's.
//    And if there isn't a problem, then why am I still writing this WHERE'S THE PR BUTTON?!
    @Column(name = "license_plate", length = 16, nullable = false, unique = true)
    private String licensePlate;

    @ManyToMany(mappedBy = "cars", fetch = FetchType.LAZY)
    private List<Customer> customers = new ArrayList<>();

    @OneToMany(mappedBy="car")
    private List<Reservation> reservations = new ArrayList<>();

//    TODO: do we need equals and hashCode?

// I haven't added brand and model columns because I don't think we need them
}
