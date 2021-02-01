package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void addCustomer(Customer customer){
        customerRepository.save(customer);
        customerRepository.flush();
    }

    public void updateCustomer(Customer customer) {
        Optional<Customer> optionalCustomer = customerRepository.findById(customer.getId());
        if(optionalCustomer.isEmpty()) {
            throw new RuntimeException("Customer is missing from the database");
        }
        Customer tmpCustomer = optionalCustomer.get();

        tmpCustomer.setRoles(customer.getRoles());
        tmpCustomer.setCars(tmpCustomer.getCars());
        tmpCustomer.setFullName(customer.getFullName());
        tmpCustomer.setPhoneNumber(customer.getPhoneNumber());
        tmpCustomer.setReservations(customer.getReservations());
        tmpCustomer.setPassword(customer.getPassword());

        customerRepository.save(tmpCustomer);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAllByOrderById();
    }



}
