package com.parking_project.parking.entity;

import javax.persistence.*;

@Entity
@Table(name=  "CUSTOMER")
public class Customer {
//    TODO: do we need a @Column annotation here too?
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

//    I don't think we need separate columns for first and last names, so I only made the one.
//    Also, I decided not to add a date of birth. I want to have as little info in the database as possible.

//    I wanted to add @NotNull and @NotEmpty, but my IntelliJ didn't recognize these annotations.
//    Either I don't have some dependencies or we don't need these annotations. I don't know.
//    If this is a problem, than it applies to the other Entities as well.
    @Column(name = "FULL_NAME", length = 128, nullable = false, unique = false)
    private String fullName;

    @Column(name = "PHONE_NUMBER", length = 16, nullable = false, unique = false)
    private String phoneNumber;

    public String getFullName() {
        return fullName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

//    TODO: many-to-many link with Car
//     this is prooobably the owner side?

//    TODO: many-to-many link with UserRole
//     this is the owner side
}
