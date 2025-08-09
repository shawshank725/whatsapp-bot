package com.example.javabot.service;

import com.example.javabot.entity.Appointment;
import com.example.javabot.entity.Order;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class OrderService {

    public String saveOrder(Order order) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference docRef = dbFirestore.collection("orders").document();
        ApiFuture<WriteResult> future = docRef.set(order);

        future.get();
        return docRef.getId();
    }

    public Order getOrder(String documentId) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentReference documentReference = dbFirestore.collection("orders").document(documentId);
        ApiFuture<DocumentSnapshot> future = documentReference.get();
        DocumentSnapshot document = future.get();

        Order order;

        if (document.exists()){
            order = document.toObject(Order.class);
            return order;
        }
        return null;
    }

    public Order getLastOrderByPhone(String phoneNumber) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> future = dbFirestore.collection("orders")
                .whereEqualTo("phone_number", phoneNumber)
                .orderBy("place_at", Query.Direction.DESCENDING)
                .limit(1)
                .get();

        QuerySnapshot snapshot = future.get();

        if (!snapshot.isEmpty()) {
            QueryDocumentSnapshot document = snapshot.getDocuments().get(0);
            return document.toObject(Order.class);
        }

        return null;
    }



}
