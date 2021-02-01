package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("api/registration")
public class RegistrationWebServiceController {

    private final CustomerService customerService;

    public RegistrationWebServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String registration() {
        return "registration";
    }

    @PostMapping
    public String registration(@RequestParam String fullName, @RequestParam String phoneNumber, @RequestParam String password) {
        Customer customer = new Customer(fullName, phoneNumber, password);
        customerService.addCustomer(customer);
        return "availableSlots";
    }
}
