package com.example.javabot.controller;

import com.example.javabot.entity.Appointment;
import com.example.javabot.entity.Doctor;
import com.example.javabot.entity.Order;
import com.example.javabot.messages.Messages;
import com.example.javabot.service.AppointmentService;
import com.example.javabot.service.DoctorService;
import com.example.javabot.service.OrderService;
import com.example.javabot.service.WhatsappSenderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.google.cloud.Timestamp;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Controller
public class WhatsappBotController {

    private final WhatsappSenderService whatsappSenderService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final OrderService orderService;

    public WhatsappBotController(WhatsappSenderService whatsappSenderService
                                ,AppointmentService appointmentService,
                                 DoctorService doctorService,
                                 OrderService orderService) {
        this.whatsappSenderService = whatsappSenderService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.orderService = orderService;
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @Value("${verify_token}")
    private String verifyToken;

    @GetMapping("/webhook")
    public ResponseEntity<String> verifyWebhook( @RequestParam("hub.mode") String mode,   @RequestParam("hub.challenge") String challenge,
            @RequestParam("hub.verify_token") String token ) {
        if ("subscribe".equals(mode) && verifyToken.equals(token)) {
            return ResponseEntity.ok(challenge);
        } else {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Verification failed");
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) throws JsonProcessingException, ExecutionException, InterruptedException {

        if (!payload.containsKey("entry")) return ResponseEntity.ok("No entry");
        Map<String, String> userInfo = returnUserInformation(payload);
        if (userInfo.isEmpty() || userInfo.get("message") == null) {
            return ResponseEntity.ok("No user message");
        }

        String phoneNumber = userInfo.get("phoneNumber");
        String phoneNumberId = userInfo.get("phoneNumberId");
        String profileName = userInfo.get("profileName");
        String text = userInfo.get("message");

        if (text.equalsIgnoreCase("hi") || text.equalsIgnoreCase("0") ||text.equalsIgnoreCase("hello")   ) {
            whatsappSenderService.sendInteractiveMessage(phoneNumber, new Messages().welcomeMessage());
            return ResponseEntity.ok("EVENT_RECEIVED");
        }

        if (text.equalsIgnoreCase("book doctor appointment")) {
            whatsappSenderService.sendInteractiveMessage(phoneNumber, new Messages().bookDoctorAppointmentMessage("book-doctor-appointment.txt"));
            return ResponseEntity.ok("EVENT_RECEIVED");
        }

        if (text.equalsIgnoreCase("order medicines")) {
            whatsappSenderService.sendInteractiveMessage(phoneNumber, new Messages().orderMedicinesMessage("order-medicine.txt"));
            return ResponseEntity.ok("EVENT_RECEIVED");
        }

        if (text.toLowerCase().contains("medicine") && text.toLowerCase().contains("address") ){
            text = text.toLowerCase();
            String[] array = text.split("address: ");
            String medicines = array[0].replace("medicines: ", "");
            String address = array[1];

            Order order = new Order(phoneNumber, medicines, address);
            String placedOrderId = orderService.saveOrder(order);
            Order placedOrder = orderService.getOrder(placedOrderId);

            whatsappSenderService.sendMessage(phoneNumber,
                    "Order placed. It will be delivered at location: "
                            + placedOrder.getDeliveredToLocation()
                            + "\nItems are - " + placedOrder.getItems());

            return ResponseEntity.ok("EVENT_RECEIVED");
        }


        ResponseEntity<?> orderingResponse = orderingMedicine(userInfo);
        if (orderingResponse != null) {
            return (ResponseEntity<String>) orderingResponse;
        }

        ResponseEntity<?> bookingResponse = appointmentBooking(userInfo);
        if (bookingResponse != null) {
            whatsappSenderService.sendMessage(phoneNumber,"Type 0 to go back to the main menu");
            return (ResponseEntity<String>) bookingResponse;
        }

        whatsappSenderService.sendMessage(phoneNumber, "Sorry, I didn’t understand that. Type 'hi' to start again.");
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    public ResponseEntity orderingMedicine(Map<String, String> userInfo) throws ExecutionException, InterruptedException {
        String text = userInfo.get("message");
        String phoneNumber = userInfo.get("phoneNumber");
        String phoneNumberId = userInfo.get("phoneNumberId");
        String profileName = userInfo.get("profileName");

        switch (text.toLowerCase()) {
            case "talk to a pharmacist" : {
                whatsappSenderService.sendMessage(
                        phoneNumber,
                        "Here is the number of a pharmacist:- +91 463463463463");
                return ResponseEntity.ok("EVENT_RECEIVED");
            }

            case "repeat last order": {
                Order lastOrder = orderService.getLastOrderByPhone(phoneNumber);
                if (lastOrder == null){
                    whatsappSenderService.sendMessage(
                            phoneNumber,
                            "You didn't place any order. Select other options to place one.");
                    return ResponseEntity.ok("EVENT_RECEIVED");
                }
                else {
                    whatsappSenderService.sendMessage(
                            phoneNumber,
                            "Here is your previous order:- \n"
                                    + "\nOrdered Items:- " + lastOrder.getItems()
                                    + "\nDelivering to location:- " + lastOrder.getDeliveredToLocation() );
                    whatsappSenderService.sendMessage(phoneNumber, "Order placed. It will be delivered to your location in a few minutes.");
                    return ResponseEntity.ok("EVENT_RECEIVED");
                }
            }

            case "specify medicines" : {
                whatsappSenderService.sendMessage(
                        phoneNumber,
                        "Write down your medicines separated by comma and address in this format:- \nMedicines: a,b,c\nAddress:- address");
                return ResponseEntity.ok("EVENT_RECEIVED");
            }
        }
        return null;
    }

    public ResponseEntity appointmentBooking(Map<String, String> userInfo) throws ExecutionException, InterruptedException {
        String text = userInfo.get("message");
        String phoneNumber = userInfo.get("phoneNumber");
        String phoneNumberId = userInfo.get("phoneNumberId");
        String profileName = userInfo.get("profileName");

        switch (text.toLowerCase()) {
            case "general physician" : {
                ResponseEntity<?> appointmentResponse = appointmentFunction("General Physician", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "dermatologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Dermatologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "pediatrician": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Pediatrician", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }
        }

        return null;
    }

    public ResponseEntity appointmentFunction(String doctorType, String phoneNumber) throws ExecutionException, InterruptedException {
        if (doctorType.contains(" ")){
            doctorType = doctorType.replace(" ", "-");
        }
        doctorType = doctorType.toLowerCase();
        System.out.println(doctorType);
        Doctor doctor = doctorService.findDoctor(doctorType);

        whatsappSenderService.sendMessage(phoneNumber, "Booking a "+ doctorType + " for you...");
        Appointment appointment = new Appointment(doctorType,
                phoneNumber, (float) doctor.getAppointmentFees(), Timestamp.now(), Timestamp.ofTimeSecondsAndNanos(Instant.now().getEpochSecond() + 86400, 0));

        String savedAppointmentId = appointmentService.saveAppointment(appointment);
        System.out.println("this is the SAVED APPOINTMENT ID ---------- " + savedAppointmentId);
        Appointment savedAppointment = appointmentService.getAppointment(savedAppointmentId);

        whatsappSenderService.sendMessage(phoneNumber,
                "A "+ doctorType + " has been booked for you with Appointment No. "
                        + savedAppointment.getId()
                        + ". Appointment fees is ₹"
                        + savedAppointment.getAppointmentFees()
                        + ". \nFor further information, you can contact the doctor on the number: "
                        + formattedNumber(doctor.getDoctorPhone()));

        return ResponseEntity.ok("EVENT_RECEIVED");
    }


    public String formattedNumber(String number){
        String formatted = "+";
        return formatted + number.substring(0,2) + " " + number.substring(3);
    }

    public Map<String,String> returnUserInformation(Map<String, Object> payload){

        if (!((Map<?, ?>) ((List<?>) payload.get("entry")).get(0)).toString().contains("messages")) {
            System.out.println("No user message received — probably a status or delivery update.");
            return Collections.emptyMap();
        }

        Map<String, String > userInfo = new HashMap<>();
        Object entry = payload.get("entry");
        if (entry instanceof List){
            List<?> entryList = (List<?>) entry;
            Map<String,Object> entryMap = (Map<String, Object>) entryList.get(0);

            List<Map<String, Object>> changes = (List<Map<String, Object>>) entryMap.get("changes");
            Map<String, Object> changesMapping = (Map<String, Object>) changes.get(0);
            Map<String, Object> valueObject =( Map<String, Object>) changesMapping.get("value");
            Map<String, Object> metadataObject = (Map<String, Object>) valueObject.get("metadata");
            String phoneNumberId = (String) metadataObject.get("phone_number_id");

            List<Map<String, Object>> messageList= (List<Map<String, Object>>) valueObject.get("messages");
            if (messageList == null || messageList.isEmpty() ) {
                return new HashMap<String, String>() ;
            }

            Map<String, Object> messageDictionary = messageList.get(0);
            String messageType = (String) messageDictionary.get("type");

            String text = "";
            if ("text".equals(messageType)) {
                Map<String, Object> textMapping = (Map<String, Object>) messageDictionary.get("text");
                if (textMapping != null) {
                    text = (String) textMapping.get("body");
                }
            } else if ("interactive".equals(messageType)) {
                Map<String, Object> interactive = (Map<String, Object>) messageDictionary.get("interactive");
                String interactiveType = (String) interactive.get("type");

                if ("button_reply".equals(interactiveType)) {
                    Map<String, Object> buttonReply = (Map<String, Object>) interactive.get("button_reply");
                    text = (String) buttonReply.get("title");
                } else if ("list_reply".equals(interactiveType)) {
                    Map<String, Object> listReply = (Map<String, Object>) interactive.get("list_reply");
                    text = (String) listReply.get("title");
                }
            }
            String phoneNumber = (String) messageDictionary.get("from");

            List<Map<String, Object>> contacts = (List<Map<String, Object>>) valueObject.get("contacts");
            String profileName = (String) ((Map<String, Object>) (contacts.get(0)).get("profile")).get("name");

            userInfo.put("phoneNumber", phoneNumber);
            userInfo.put("phoneNumberId" , phoneNumberId);
            userInfo.put("message", text);
            userInfo.put("profileName", profileName);
        }
        return userInfo;
    }
}
