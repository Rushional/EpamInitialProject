package com.parking_project.parking.entity;

import javax.persistence.*;
import java.util.ArrayList;

@Entity
@Table(name = "user_role")
public class UserRole {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "role_name", length = 32, nullable = false, unique = true)
    private String roleName;

    public String getRoleName() {
        return roleName;
    }

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private ArrayList<Customer> customers = new ArrayList<>();
}
