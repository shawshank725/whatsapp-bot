package com.example.javabot.service;

import com.example.javabot.entity.Appointment;
import com.example.javabot.entity.Doctor;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DoctorService {

    List<Doctor> findAllDoctors();

    Doctor saveDoctor(Doctor doctor);
    Doctor findDoctorById(int theId);
    Doctor findDoctorByDoctorType(String doctorType);


}
