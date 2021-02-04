package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("login")
public class LoginWebServiceController {

    private final CustomerService customerService;

    public LoginWebServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    public String login() {
        return "login";
    }

    @PostMapping
    public String login(@RequestParam String fullName, @RequestParam String password) {
        Customer customer = customerService.getCustomerByFullName(fullName);
        if (customer != null && customer.getPassword().equals(password)){
                return "redirect:api/availableSlots";
        }
        return "login";
    }
}
