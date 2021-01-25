package com.parking_project.parking.entity;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "customer")
public class Customer {
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

/*
* I don't think we need separate columns for first and last names, so I only made the one.
* Also, I decided not to add a date of birth. I want to have as little info in the database as possible.
*
* I wanted to add @NotNull and @NotEmpty, but my IntelliJ didn't recognize these annotations.
* Either I don't have some dependencies or we don't need these annotations. I don't know.
* If this is a problem, than it applies to the other Entities as well.
*/
    @Column(name = "full_name", length = 128, nullable = false, unique = false)
    private String fullName;

    @Column(name = "phone_number", length = 16, nullable = false, unique = false)
    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_car",
            joinColumns = { @JoinColumn(name = "customer_id")},
            inverseJoinColumns = { @JoinColumn(name = "car_id") }
    )
    ArrayList<Car> cars = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_role",
            joinColumns = { @JoinColumn(name = "customer_id")},
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    ArrayList<Car> roles = new ArrayList<>();

}
