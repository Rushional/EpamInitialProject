package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.Role;
import com.parking_project.parking.data.repositoty.CarRepository;
import com.parking_project.parking.data.repositoty.CustomerCarRepository;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
    private final CustomerCarRepository customerCarRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CarRepository carRepository, CustomerCarRepository customerCarRepository) {
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
        this.customerCarRepository = customerCarRepository;
    }

    public void addCustomer(Customer customer){
        customer.setRoles(Collections.singleton(Role.USER));
        customerRepository.save(customer);
        customerRepository.flush();
    }

    public void updateCustomer(Customer customer) {
        Customer tmpCustomer = getCustomerById(customer.getId().toString());

        tmpCustomer.setId(customer.getId());
        tmpCustomer.setRoles(customer.getRoles());
        tmpCustomer.setCars(tmpCustomer.getCars());
        tmpCustomer.setFullName(customer.getFullName());
        tmpCustomer.setPhoneNumber(customer.getPhoneNumber());
        tmpCustomer.setReservations(customer.getReservations());
        tmpCustomer.setPassword(customer.getPassword());

        customerRepository.save(tmpCustomer);
        customerRepository.flush();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAllByOrderById();
    }

    public Customer getCustomerByFullName(String fullName) {
        return customerRepository.findByFullName(fullName);
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Customer is missing from the database"));
    }

    public List<Customer> getCustomersByCar(String carId){
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car is missing from the database"));
        return customerCarRepository.findCustomersByCar(car);
    }

}
