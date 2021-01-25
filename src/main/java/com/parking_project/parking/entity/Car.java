package com.parking_project.parking.entity;

// Not sure if that's the right import - I totally might be using wrong annotations
import javax.persistence.*;

// Not sure if table name should be in uppercase or lowercase
@Entity
@Table(name = "CAR")
public class Car {
    @Id
//    TODO: Will we have a problem with id auto generation?
//     I mean, it might want to generate ID's automatically, and we don't want that
    @Column(name = "LICENSE_PLATE", length = 16, nullable = false, unique = true)
    private String licensePlate;

    public String getLicensePlate() {
        return licensePlate;
    }

//    TODO: many-to-many link with Customer
//     inverse side I think

//    TODO: do we need equals and hashCode?

// I haven't added brand and model columns because I don't think we need them
}
