package com.example.javabot.entity;

import com.google.cloud.Timestamp;

import java.time.LocalDateTime;

public class Appointment {

    public Appointment() {}

    public Appointment(String doctorType, String userPhone,
                       float appointmentFees, Timestamp  registeredAt, Timestamp  appointmentAt) {
        this.doctorType = doctorType;
        this.userPhone = userPhone;
        this.appointmentFees = appointmentFees;
        this.registeredAt = registeredAt;
        this.appointmentAt = appointmentAt;
    }

    private int id;

    private String doctorType;

    private String userPhone;

    private float appointmentFees;
    private Timestamp  registeredAt;
    private Timestamp appointmentAt;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public float getAppointmentFees() {
        return appointmentFees;
    }

    public void setAppointmentFees(float appointmentFees) {
        this.appointmentFees = appointmentFees;
    }

    public Timestamp  getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(Timestamp  registeredAt) {
        this.registeredAt = registeredAt;
    }
}
