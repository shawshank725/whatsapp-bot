package com.example.javabot.service;

import com.example.javabot.entity.Appointment;
import com.example.javabot.repository.AppointmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AppointmentServiceImpl implements AppointmentService{

    private final AppointmentRepository appointmentRepository;

    public AppointmentServiceImpl(AppointmentRepository appointmentRepository){
        this.appointmentRepository = appointmentRepository;
    }

    @Override
    public List<Appointment> findAllAppointments() {
        return List.of();
    }

    @Override
    public Appointment saveAppointment(Appointment appointment) {
        Appointment savedAppointment = appointmentRepository.save(appointment);
        return savedAppointment;
    }
}
