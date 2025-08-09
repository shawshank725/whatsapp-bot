package com.example.javabot.entity;

import com.google.cloud.Timestamp;
import com.google.cloud.firestore.annotation.PropertyName;

public class Order {

    private String phoneNumber;
    private String items;
    private Timestamp placeAt;
    private String deliveredToLocation;

    public Order() {}

    public Order(String phoneNumber, String items, String deliveredToLocation) {
        this.phoneNumber = phoneNumber;
        this.items = items;
        this.deliveredToLocation = deliveredToLocation;
        this.placeAt = Timestamp.now();
    }

    @PropertyName("phone_number")
    public String getPhoneNumber() {
        return phoneNumber;
    }

    @PropertyName("phone_number")
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    @PropertyName("items")
    public String getItems() {
        return items;
    }

    @PropertyName("items")
    public void setItems(String items) {
        this.items = items;
    }

    @PropertyName("place_at")
    public Timestamp getPlaceAt() {
        return placeAt;
    }

    @PropertyName("place_at")
    public void setPlaceAt(Timestamp placeAt) {
        this.placeAt = placeAt;
    }

    @PropertyName("delivered_to_location")
    public String getDeliveredToLocation() {
        return deliveredToLocation;
    }

    @PropertyName("delivered_to_location")
    public void setDeliveredToLocation(String deliveredToLocation) {
        this.deliveredToLocation = deliveredToLocation;
    }
}
