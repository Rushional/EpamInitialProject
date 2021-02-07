package com.parking_project.parking.business.service;

import com.parking_project.parking.data.entity.ParkingSlot;

import java.util.List;

public interface BookingService {
    public List<ParkingSlot> getAllSlots();

    public List<ParkingSlot> getAvailableSlots();
}
