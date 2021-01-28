package com.parking_project.parking.repositoty;

import com.parking_project.parking.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarRepository extends JpaRepository<Car, String> {
}
