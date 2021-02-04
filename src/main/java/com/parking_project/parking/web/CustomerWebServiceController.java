package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CarService;
import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
public class CustomerWebServiceController {

    private final CustomerService customerService;
    public final CarService carService;

    @Autowired
    public CustomerWebServiceController(CustomerService customerService, CarService carService) {
        this.customerService = customerService;
        this.carService = carService;
    }

    @GetMapping()
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping()
    public ResponseEntity<Void> addCustomer(@RequestBody Customer customer, UriComponentsBuilder builder) {
        customerService.addCustomer(customer);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/api/customer/{id}").buildAndExpand(customer.getId()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    @PutMapping()
    public ResponseEntity<Customer> updateCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return new ResponseEntity<Customer>(customer, HttpStatus.OK);
    }

    @GetMapping("/find")
    public List<Customer> getCustomersByCar(@RequestParam String id) {
        return customerService.getCustomersByCar(id);}

}
