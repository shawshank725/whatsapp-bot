package com.example.javabot.service;

import com.example.javabot.entity.Doctor;
import com.example.javabot.repository.AppointmentRepository;
import com.example.javabot.repository.DoctorRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DoctorServiceImpl implements DoctorService{

    private final DoctorRepository doctorRepository;

    public DoctorServiceImpl(DoctorRepository doctorRepository){
        this.doctorRepository = doctorRepository;
    }


    @Override
    public List<Doctor> findAllDoctors() {
        return List.of();
    }

    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return null;
    }

    @Override
    public Doctor findDoctorById(int theId) {
        Optional<Doctor> optionalDoctor = doctorRepository.findById(theId);
        if (optionalDoctor.isPresent()){
            return optionalDoctor.get();
        }
        return null;
    }

    @Override
    public Doctor findDoctorByDoctorType(String doctorType) {
        return doctorRepository.findDoctorByDoctorType(doctorType);
    }
}
