package com.example.javabot.messages;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Messages {

    public String readMessage(String filename){
        StringBuilder data = new StringBuilder();
        try (Scanner scanner = new Scanner(getClass().getClassLoader().getResourceAsStream(filename))) {
            while (scanner.hasNextLine()) {
                data.append(scanner.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Error reading file: " + filename);
            e.printStackTrace();
        }
        return data.toString();
    }


    public Map<String, Object> welcomeMessage() throws JsonProcessingException {
        String jsonStr = readMessage("welcome.txt");
        Map<String, Object> mapping = new ObjectMapper().readValue(jsonStr, HashMap.class);
        return mapping;
    }

    public Map<String, Object> bookDoctorAppointmentMessage(String filepath) throws JsonProcessingException {
        String jsonStr = readMessage(filepath);
        Map<String, Object> mapping = new ObjectMapper().readValue(jsonStr, HashMap.class);
        return mapping;
    }

    public Map<String, Object> orderMedicinesMessage(String filepath) throws JsonProcessingException {
        String jsonStr = readMessage(filepath);
        Map<String, Object> mapping = new ObjectMapper().readValue(jsonStr, HashMap.class);
        return mapping;
    }




}
