package com.example.javabot.service;

import com.example.javabot.entity.Appointment;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.WriteResult;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class AppointmentService  {

    public String saveAppointment(Appointment appointment) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("appointments").document();
        ApiFuture<WriteResult> future = docRef.set(appointment);

        future.get();
        return docRef.getId();
    }

    public Appointment getAppointment(String documentId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("appointments").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Appointment appointment ;

        if (document.exists()){
            appointment = document.toObject(Appointment.class);
            return appointment;
        }
        return null;
    }



}
