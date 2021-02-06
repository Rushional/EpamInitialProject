package com.parking_project.parking.business.service;

import com.parking_project.parking.conf.PasswordEncoder;
import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.Role;
import com.parking_project.parking.data.repositoty.CarRepository;
import com.parking_project.parking.data.repositoty.CustomerCarRepository;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    private final CarRepository carRepository;
    private final CustomerCarRepository customerCarRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, CarRepository carRepository,
                           CustomerCarRepository customerCarRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.carRepository = carRepository;
        this.customerCarRepository = customerCarRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addCustomer(String fullName, String password, String phoneNumber){
        Customer customer = new Customer();
        customer.setFullName(fullName);
        customer.setPhoneNumber(phoneNumber);
        customer.setPassword(passwordEncoder.getPasswordEncoder().encode(password));
        customer.setRoles(Collections.singleton(Role.USER));
        customerRepository.save(customer);
        customerRepository.flush();
    }

    public void updateCustomer(Customer customer) {
        Customer tmpCustomer = getCustomerById(customer.getId().toString());

        if (tmpCustomer.getPassword().length() < 20) {
            tmpCustomer.setPassword(passwordEncoder.getPasswordEncoder().encode(customer.getPassword()));
        }

        tmpCustomer.setId(customer.getId());
        tmpCustomer.setCars(tmpCustomer.getCars());
        tmpCustomer.setFullName(customer.getFullName());
        tmpCustomer.setPhoneNumber(customer.getPhoneNumber());
        //recursion problem
//        tmpCustomer.setReservations(customer.getReservations());

        customerRepository.save(tmpCustomer);
        customerRepository.flush();
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAllByOrderById();
    }

    public Customer getCustomerById(String id) {
        return customerRepository.findById(Long.valueOf(id))
                .orElseThrow(() -> new RuntimeException("Customer is missing from the database"));
    }

    public List<Customer> getCustomersByCar(String carId){
        Car car = carRepository.findById(carId).orElseThrow(() -> new RuntimeException("Car is missing from the database"));
        return customerCarRepository.findCustomersByCar(car);
    }

    @Override
    public UserDetails loadUserByUsername(String fullName) throws UsernameNotFoundException {
        return customerRepository.findByFullName(fullName);
    }
}
