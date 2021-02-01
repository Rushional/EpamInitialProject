package com.parking_project.parking.data.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "user_role")
@NoArgsConstructor
@Setter
@Getter
public class UserRole {
    @Id
    @Column(name = "role_id")
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(name = "role_name", length = 32, nullable = false, unique = true)
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private List<Customer> customers = new ArrayList<>();
}
