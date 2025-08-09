package com.example.javabot.entity;

import jakarta.persistence.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "appointments")
public class Appointment {

    public Appointment(String doctorType, String userPhone, float appointmentFees, LocalDateTime registeredAt, LocalDateTime appointmentAt) {
        this.doctorType = doctorType;
        this.userPhone = userPhone;
        this.appointmentFees = appointmentFees;
        this.registeredAt = registeredAt;
        this.appointmentAt = appointmentAt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "doctor_type", nullable = false)
    private String doctorType;

    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @Column(name = "appointment_fees")
    private float appointmentFees;

    @Column(name = "registered_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime registeredAt;

    @Column(name = "appointment_at")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentAt;


    public Appointment() {    }

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

    public LocalDateTime getRegisteredAt() {
        return registeredAt;
    }

    public void setRegisteredAt(LocalDateTime registeredAt) {
        this.registeredAt = registeredAt;
    }
}
