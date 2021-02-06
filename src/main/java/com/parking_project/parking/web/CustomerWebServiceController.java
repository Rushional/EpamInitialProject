package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CarService;
import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CustomerWebServiceController {

    private final CustomerService customerService;
    public final CarService carService;

    @Autowired
    public CustomerWebServiceController(CustomerService customerService, CarService carService) {
        this.customerService = customerService;
        this.carService = carService;
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PostMapping("/customer/create")
    public ResponseEntity<Void> addCustomer(@RequestParam String fullName, @RequestParam String password,
                                            @RequestParam String phoneNumber) {
        customerService.addCustomer(fullName, password, phoneNumber);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PutMapping("/customer/update")
    public ResponseEntity<Void> updateCustomer(@RequestBody Customer customer) {
        customerService.updateCustomer(customer);
        return new ResponseEntity<Void>(HttpStatus.OK);
    }

    @GetMapping("/customers/{id}/find")
    public List<Customer> getCustomersByCar(@PathVariable String id) {
        return customerService.getCustomersByCar(id);
    }

}
