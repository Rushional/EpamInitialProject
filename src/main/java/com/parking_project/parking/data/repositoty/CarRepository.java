package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CarRepository extends JpaRepository<Car, String> {

}
