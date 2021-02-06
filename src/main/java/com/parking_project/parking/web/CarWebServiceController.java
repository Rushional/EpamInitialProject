package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CarService;
import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class CarWebServiceController {
    private final CarService carService;

    @Autowired
    public CarWebServiceController(CarService carService) {
        this.carService = carService;
    }

    @PreAuthorize(value = "hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/car/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") String id) {
        Car car = carService.getCarById(id);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }
    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping()
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/car/create")
    public ResponseEntity<Void> addCar(@RequestParam String licensePlate, @RequestParam String customerId) {
        carService.addCar(licensePlate, customerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PutMapping("/car/{id}")
    public ResponseEntity<Void> updateCar(@RequestBody Car car, UriComponentsBuilder builder) {
        carService.updateCar(car);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/update/{id}").buildAndExpand(car.getLicensePlate()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PreAuthorize(value = "hasAuthority('USER') or hasAuthority('ADMIN')")
    @GetMapping("/{id}/cars")
    public List<Car> getCarsByCustomer(@PathVariable String id) {
        return carService.getCarsByCustomer(id);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeCarById(@RequestParam String licensePlate) {
        carService.removeCar(licensePlate);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
