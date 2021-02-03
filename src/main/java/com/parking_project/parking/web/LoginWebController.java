package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginWebController {
    private final CustomerService customerService;

    public LoginWebController(CustomerService customerService) {
        this.customerService = customerService;
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
