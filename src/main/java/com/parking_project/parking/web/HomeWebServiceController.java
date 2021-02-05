package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Customer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@PreAuthorize(value = "hasAuthority('USER') or hasAuthority('ADMIN')")
public class HomeWebServiceController {

    private final CustomerService customerService;

    public HomeWebServiceController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping("/home")
    public String home(@AuthenticationPrincipal Customer customer, Model model) {
        if (customer != null) {
            model.addAttribute("customer", customer.getFullName());
            model.addAttribute("role", customer.getRoles());
            return "home";
        }
        model.addAttribute("customer", "anonymous");
        return "home";
    }
    @GetMapping("/")
    public String toCustomer() {
        return "redirect:/home";
    }
}
