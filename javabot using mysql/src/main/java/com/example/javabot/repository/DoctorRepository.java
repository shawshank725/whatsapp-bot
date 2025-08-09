package com.example.javabot.repository;

import com.example.javabot.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.print.Doc;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Integer> {

    Doctor findDoctorByDoctorType(String doctorType);
}
