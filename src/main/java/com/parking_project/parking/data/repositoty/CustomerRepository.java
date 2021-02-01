package com.parking_project.parking.data.repositoty;

import com.parking_project.parking.data.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findById(long customerId);
    List<Customer> findAllByOrderById();
    Customer findByFullName(String fullName);
}
