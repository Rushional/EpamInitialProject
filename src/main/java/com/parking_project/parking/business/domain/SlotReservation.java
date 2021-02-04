package com.parking_project.parking.business.domain;


import com.parking_project.parking.data.entity.StatusType;

import java.time.LocalDateTime;
import java.util.Date;

public class SlotReservation {
    private long slotId;
    private long carId;
    private long customer_id;
    private String description;
    private String customerName;
    private Date start;
    private Date end;

    public long getSlotId() {
        return slotId;
    }

    public void setSlotId(long slotId) {
        this.slotId = slotId;
    }

    public long getCar_id() {
        return carId;
    }

    public void setCar_id(long car_id) {
        this.carId = car_id;
    }

    public long getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(long customer_id) {
        this.customer_id = customer_id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "SlotReservation{" +
                "slotId=" + slotId +
                ", carId=" + carId +
                ", customer_id=" + customer_id +
                ", description='" + description + '\'' +
                ", customerName='" + customerName + '\'' +
                ", start=" + LocalDateTime.from(start.toInstant()) +
                ", end=" + end +
                '}';
    }
}
