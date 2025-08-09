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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.*;

@Controller
public class WhatsappBotController {

    private final WhatsappSenderService whatsappSenderService;
    private final AppointmentService appointmentService;
    private final DoctorService doctorService;
    private final OrderService orderService;

    public WhatsappBotController(WhatsappSenderService whatsappSenderService,
                                 AppointmentService appointmentService, DoctorService doctorService,
                                 OrderService orderService) {
        this.whatsappSenderService = whatsappSenderService;
        this.appointmentService = appointmentService;
        this.doctorService = doctorService;
        this.orderService = orderService;
    }

    @Value("${verify_token}")
    private String verifyToken;


    @GetMapping("/")
    public String index(){
        return "index";
    }

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
    public ResponseEntity<String> receiveMessage(@RequestBody Map<String, Object> payload) throws JsonProcessingException {

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

        if (text.equalsIgnoreCase("view more doctors")) {
            whatsappSenderService.sendInteractiveMessage(phoneNumber, new Messages().bookDoctorAppointmentMessage("more-doctors.txt"));
            return ResponseEntity.ok("EVENT_RECEIVED");
        }

        if (text.toLowerCase().contains("medicine") && text.toLowerCase().contains("address") ){
            text = text.toLowerCase();
            String[] array = text.split("address: ");
            String medicines = array[0].replace("medicines: ", "");
            String address = array[1];

            Order order = new Order(phoneNumber, medicines, address);
            Order placedOrder = orderService.saveOrder(order);
            whatsappSenderService.sendMessage(phoneNumber,
                    "Order placed. It will be delivered at location: "
                    + placedOrder.getDeliveredToLocation()
                    + "\nItems are - " + placedOrder.getItems());

            return ResponseEntity.ok("EVENT_RECEIVED");
        }


        ResponseEntity<?> bookingResponse = appointmentBooking(userInfo);
        if (bookingResponse != null) {
            whatsappSenderService.sendMessage(
                    phoneNumber,
                    "Type 0 to go back to the main menu");
            return (ResponseEntity<String>) bookingResponse;
        }

        ResponseEntity<?> orderingResponse = orderingMedicine(userInfo);
        if (orderingResponse != null) {
            //whatsappSenderService.sendMessage(phoneNumber, "Type 0 to go back to the main menu");
            return (ResponseEntity<String>) orderingResponse;
        }

        whatsappSenderService.sendMessage(phoneNumber, "Sorry, I didn’t understand that. Type 'hi' to start again.");
        return ResponseEntity.ok("EVENT_RECEIVED");
    }

    public ResponseEntity orderingMedicine(Map<String, String> userInfo){
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
                List<Order> orders = orderService.findAllOrdersByPhoneNumber(phoneNumber);
                if (orders.size() == 0){
                    whatsappSenderService.sendMessage(
                            phoneNumber,
                            "You didn't place any order. Select other options to place one.");
                    return ResponseEntity.ok("EVENT_RECEIVED");
                }
                else {
                    Order previousOrder = orders.get(0);
                    whatsappSenderService.sendMessage(
                            phoneNumber,
                            "Here is your previous order:- \n"
                                    + "Order ID: "+ previousOrder.getId()
                                    + "\nOrdered Items:- " + previousOrder.getItems()
                                    +  "\nDelivering to location:- " + previousOrder.getPlaceAt() );
                    whatsappSenderService.sendMessage(phoneNumber, "Order placed. It will be delivered shortly to your location.");
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

    public ResponseEntity appointmentBooking(Map<String, String> userInfo){
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
            case "cardiologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Cardiologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "orthopedic": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Orthopedic", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "neurologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Neurologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "ent": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("ENT", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "gynecologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Gynecologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "psychiatrist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Psychiatrist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "urologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Urologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "gastroenterologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Gastroenterologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "oncologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Oncologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "nephrologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Nephrologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "pulmonologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Pulmonologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "endocrinologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Endocrinologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "rheumatologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Rheumatologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "ophthalmologist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Ophthalmologist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "dentist": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Dentist", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }

            case "homeopath": {
                ResponseEntity<?> appointmentResponse = appointmentFunction("Homeopath", phoneNumber);
                if (appointmentResponse != null) return (ResponseEntity<String>) appointmentResponse;
            }
        }

        return null;
    }

    public ResponseEntity appointmentFunction(String doctorType, String phoneNumber){
        Doctor doctor = doctorService.findDoctorByDoctorType(doctorType);

        whatsappSenderService.sendMessage(phoneNumber, "Booking a "+ doctorType + " for you...");
        Appointment appointment = new Appointment(doctorType,
                phoneNumber, (float) doctor.getAppointmentFees(), LocalDateTime.now(), LocalDateTime.now().plusDays(1));

        Appointment savedAppointment = appointmentService.saveAppointment(appointment);
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
