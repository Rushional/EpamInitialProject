package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.Role;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomerService(CustomerRepository customerRepository, PasswordEncoder passwordEncoder) {
        this.customerRepository = customerRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void addCustomer(Customer customer){
        customer.setRoles(Collections.singleton(Role.USER));
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
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

    public Customer getCustomerByFullName(String fullName) {
        return customerRepository.findByFullName(fullName);
    }

    @Override
    public UserDetails loadUserByUsername(String fullName) throws UsernameNotFoundException {
        return customerRepository.findByFullName(fullName);
    }
}
