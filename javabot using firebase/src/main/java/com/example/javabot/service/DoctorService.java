package com.example.javabot.service;

import com.example.javabot.entity.Appointment;
import com.example.javabot.entity.Doctor;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class DoctorService {

    public Doctor findDoctor(String documentId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("doctors").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Doctor doctor ;

        if (document.exists()){
            doctor = document.toObject(Doctor.class);
            return doctor;
        }
        return null;
    }
}
