package com.example.javabot.service;

import com.example.javabot.messages.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class WhatsappSenderService {

    @Value("${whatsapp.api.token}")
    private String accessToken;

    @Value("${phone_number_id}")
    private String phoneNumberId;

    public void sendMessage(String toPhone, String messageText) {
        String url = "https://graph.facebook.com/v19.0/" + phoneNumberId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("messaging_product", "whatsapp");
        messageBody.put("to", toPhone);
        messageBody.put("type", "text");

        Map<String, String> text = new HashMap<>();
        text.put("body", messageText);
        messageBody.put("text", text);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(messageBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Sent message: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to send message: " + e.getStatusCode());
            System.err.println("Error message: " + e.getStatusText());
            System.err.println("Details: " + e.getResponseBodyAsString());
        }
    }

    public void sendInteractiveMessage(String toPhone,Map<String, Object> interactiveContent) {
        String url = "https://graph.facebook.com/v19.0/" + phoneNumberId + "/messages";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(accessToken);

        Map<String, Object> messageBody = new HashMap<>();
        messageBody.put("messaging_product", "whatsapp");
        messageBody.put("to", toPhone);
        messageBody.put("type", "interactive");
        messageBody.put("interactive", interactiveContent);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(messageBody, headers);

        try {
            ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
            System.out.println("Sent message: " + response.getBody());
        } catch (HttpClientErrorException e) {
            System.err.println("Failed to send message: " + e.getStatusCode());
            System.err.println("Error message: " + e.getStatusText());
            System.err.println("Details: " + e.getResponseBodyAsString());
        }
    }
}
