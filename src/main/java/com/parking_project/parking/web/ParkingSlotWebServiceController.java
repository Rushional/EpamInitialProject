package com.parking_project.parking.web;

import com.parking_project.parking.business.service.ParkingSlotService;
import com.parking_project.parking.data.entity.ParkingSlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/parking/slots")
public class ParkingSlotWebServiceController {
    private final ParkingSlotService parkingSlotService;

    @Autowired
    public ParkingSlotWebServiceController(ParkingSlotService parkingSlotService) {
        this.parkingSlotService = parkingSlotService;
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @GetMapping()
    public List<ParkingSlot> getAllSlots() {
        return this.parkingSlotService.getAllSlots();
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @PostMapping("/add")
    public ResponseEntity<Void> addParkingSlot(@RequestParam String description) {
        parkingSlotService.addParkingSlot(description);
        return new ResponseEntity<Void>(HttpStatus.CREATED);
    }

    @PreAuthorize(value = "hasAuthority('ADMIN')")
    @DeleteMapping("/remove")
    public ResponseEntity<Void> removeParkingSlotById(@RequestParam String id) {
        parkingSlotService.removeParkingSlot(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
