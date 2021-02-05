package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.Car;
import com.parking_project.parking.data.entity.Customer;
import com.parking_project.parking.data.entity.StatusType;
import com.parking_project.parking.data.repositoty.CarCustomerRepository;
import com.parking_project.parking.data.repositoty.CarRepository;
import com.parking_project.parking.data.repositoty.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CarService {
    private final CarRepository carRepository;
    private final CustomerRepository customerRepository;
    private final CarCustomerRepository carCustomerRepository;

    @Autowired
    public CarService(CarRepository carRepository, CustomerRepository customerRepository, CarCustomerRepository carCustomerRepository) {
        this.carRepository = carRepository;
        this.customerRepository = customerRepository;
        this.carCustomerRepository = carCustomerRepository;
    }

    public void addCar(String licensePlate, String customerId) {
        Car car = new Car();
        Customer customer = customerRepository.findCustomerById(Long.valueOf(customerId));
        List<Customer> customerList = new ArrayList<>();
        customerList.add(customer);
        car.setStatus(StatusType.ACTIVE);
        car.setLicensePlate(licensePlate);
        car.setCustomers(customerList);
        System.out.println(car.getCustomers());
        carRepository.save(car);
        carRepository.flush();
    }

    public void updateCar(Car car) {
        Car tmpCar = getCarById(car.getLicensePlate());
        tmpCar.setLicensePlate(car.getLicensePlate());
        tmpCar.setCustomers(car.getCustomers());
        tmpCar.setStatus(car.getStatus());

        carRepository.flush();
    }

    public void removeCar(String licensePlate) {
        Car tmpCar = getCarById(licensePlate);
        tmpCar.setStatus(StatusType.DISABLED);

        carRepository.flush();
    }

    public Car getCarById(String id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Car is missing from the database"));
    }

    public List<Car> getAllCars() {
        return carRepository.findAll();
    }

    public List<Car> getCarsByCustomers(String customerId) {
        Long id = Long.valueOf(customerId);
        Customer customer = customerRepository.findCustomerById(id);
        System.out.println(customerId);
        return carCustomerRepository.findCarsByCustomer(customer);
    }
}
