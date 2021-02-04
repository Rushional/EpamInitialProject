package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerCarRepository extends JpaRepository<Car, String> {
    @Query("SELECT u FROM Customer u LEFT JOIN FETCH u.cars WHERE :car member u.cars")
    List<Customer> findCustomersByCar(Car car);
}
