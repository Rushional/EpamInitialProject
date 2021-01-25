package com.parking_project.parking.entity;

import javax.persistence.*;

@Entity
@Table(name = "USER_ROLE")
public class UserRole {
//    TODO: do we need a @Column annotation here too?
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "ROLE_NAME", length = 32, nullable = false, unique = true)
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

//    TODO: many-to-many link with Customer
//     this is the inverse side
}
