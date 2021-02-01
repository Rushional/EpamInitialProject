package com.parking_project.parking.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "customer")
@NoArgsConstructor
@Setter
@Getter
public class Customer {
    @Id
    @Column(name = "customer_id")
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

    @Column(name = "password", length = 32, nullable = false)
    private String password;

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_car",
            joinColumns = { @JoinColumn(name = "customer_id")},
            inverseJoinColumns = { @JoinColumn(name = "license_plate") }
    )
    List<Car> cars = new ArrayList<>();

    @ManyToMany(cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
    @JoinTable(
            name = "customer_role",
            joinColumns = { @JoinColumn(name = "customer_id")},
            inverseJoinColumns = { @JoinColumn(name = "role_id") }
    )
    List<Car> roles = new ArrayList<>();

    @OneToMany(mappedBy="customer")
    private List<Reservation> reservations = new ArrayList<>();

    public Customer(String fullName, String phoneNumber, String password) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }
}
