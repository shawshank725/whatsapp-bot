package com.example.javabot.entity;


public class Doctor {

    private int id;
    private String doctorType;
    private String doctorName;
    private String doctorPhone;
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
