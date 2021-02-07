package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CarCustomerRepository extends JpaRepository<Customer, String> {
    @Query("SELECT u FROM Car u LEFT JOIN FETCH u.customers WHERE :customer member u.customers")
    List<Car> findCarsByCustomer(Customer customer);
}
