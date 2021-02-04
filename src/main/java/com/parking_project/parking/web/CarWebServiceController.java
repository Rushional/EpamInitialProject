package com.parking_project.parking.web;

import com.parking_project.parking.business.service.CarService;
import com.parking_project.parking.business.service.CustomerService;
import com.parking_project.parking.data.entity.Car;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("api/cars")
public class CarWebServiceController {
    private final CarService carService;
    private final CustomerService customerService;

    @Autowired
    public CarWebServiceController(CarService carService, CustomerService customerService) {
        this.carService = carService;
        this.customerService = customerService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCarById(@PathVariable("id") String id) {
        Car car = carService.getCarById(id);
        return new ResponseEntity<Car>(car, HttpStatus.OK);
    }

    @GetMapping()
    public List<Car> getAllCars() {
        return carService.getAllCars();
    }

    @PostMapping("/car")
    public ResponseEntity<Void> addCar(@RequestParam String licensePlate, @RequestParam String customerId, UriComponentsBuilder builder) {
        carService.addCar(licensePlate, customerId);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/add/{id}").buildAndExpand(licensePlate, customerId).toUri());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @PutMapping("/car")
    public ResponseEntity<Void> updateCar(@RequestBody Car car, UriComponentsBuilder builder) {
        carService.updateCar(car);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(builder.path("/update/{id}").buildAndExpand(car.getLicensePlate()).toUri());
        return new ResponseEntity<>(headers, HttpStatus.OK);
    }

    @GetMapping("find/{id}")
    public List<Car> getCarsByCustomers(@PathVariable("id") String id) {
        return carService.getCarsByCustomers(id);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> removeCarById(@PathVariable("id") String id) {
        carService.removeCar(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
