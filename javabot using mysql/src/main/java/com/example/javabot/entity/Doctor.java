package com.example.javabot.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "doctors")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "doctor_type", nullable = false)
    private String doctorType;

    @Column(name = "doctor_name", nullable = false)
    private String doctorName;

    @Column(name = "doctor_phone", nullable = false)
    private String doctorPhone;

    @Column(name = "appointment_fees", nullable = false)
    private double appointmentFees;

    public Doctor() {
    }

    public Doctor(String doctorType, String doctorName, String doctorPhone, double appointmentFees) {
        this.doctorType = doctorType;
        this.doctorName = doctorName;
        this.doctorPhone = doctorPhone;
        this.appointmentFees = appointmentFees;
    }

    public int getId() {
        return id;
    }

    public String getDoctorType() {
        return doctorType;
    }

    public void setDoctorType(String doctorType) {
        this.doctorType = doctorType;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDoctorPhone() {
        return doctorPhone;
    }

    public void setDoctorPhone(String doctorPhone) {
        this.doctorPhone = doctorPhone;
    }

    public double getAppointmentFees() {
        return appointmentFees;
    }

    public void setAppointmentFees(double appointmentFees) {
        this.appointmentFees = appointmentFees;
    }
}
