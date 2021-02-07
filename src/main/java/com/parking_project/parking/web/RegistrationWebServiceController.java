package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.Role;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

@Controller
public class RegistrationWebServiceController {

    private final CustomerService customerService;

    public RegistrationWebServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("registration")
    public String registration(@RequestParam String fullName, @RequestParam String phoneNumber, @RequestParam String password) {
        customerService.addCustomer(fullName, password, phoneNumber);
        return "redirect:api/parking/slots/available";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
