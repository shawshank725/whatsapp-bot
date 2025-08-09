package com.example.javabot.service;

import com.example.javabot.entity.Appointment;

import java.util.List;

public interface AppointmentService  {

    List<Appointment> findAllAppointments();

    Appointment saveAppointment(Appointment appointment);


}
