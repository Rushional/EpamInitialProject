package com.parking_project.parking.business.domain;

public class SlotReservation {
    private long id;
    private long car_id;
    private long customer_id;
    private String description;
    private String status;
    private String customerName;

    public long getSlot_id() {
        return id;
    }

    public void setId(long slot_id) {
        this.id = slot_id;
    }

    public long getCar_id() {
        return car_id;
    }

    public void setCar_id(long car_id) {
        this.car_id = car_id;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
}
