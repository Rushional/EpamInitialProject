package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@PreAuthorize(value = "hasAuthority('USER')")
public class HomeController {

    private final CustomerService customerService;

    public HomeController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @PreAuthorize(value = "hasAuthority('USER')")
    public String home(@AuthenticationPrincipal Customer customer, Model model) {
        if (customer != null) {
            model.addAttribute("customer", customer.getFullName());
            return "home";
        }
        model.addAttribute("customer", "anonymous");
        return "home";
    }
}
