package com.example.javabot.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private String items;

    @Column(name = "total_cost", nullable = false)
    private int totalCost;

    @Column(name = "place_at")
    private LocalDateTime placeAt;

    @Column(name = "delivered_to_location")
    private String deliveredToLocation;

    // Constructors
    public Order() {}

    public Order(String phoneNumber, String items, String deliveredToLocation) {
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.deliveredToLocation = deliveredToLocation;
        this.placeAt = LocalDateTime.now();
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getItems() {
        return items;
    }

    public void setItems(String items) {
        this.items = items;
    }

    public int getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(int totalCost) {
        this.totalCost = totalCost;
    }

    public LocalDateTime getPlaceAt() {
        return placeAt;
    }

    public void setPlaceAt(LocalDateTime placeAt) {
        this.placeAt = placeAt;
    }

    public String getDeliveredToLocation() {
        return deliveredToLocation;
    }

    public void setDeliveredToLocation(String deliveredToLocation) {
        this.deliveredToLocation = deliveredToLocation;
    }
}
