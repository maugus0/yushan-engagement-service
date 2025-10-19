package com.yushan.engagement_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Component
public class GamificationServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.gamification.url:http://localhost:8085}")
    private String gamificationServiceUrl;

    public GamificationServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void addExp(UUID userId, Float exp) {
        try {
            String url = gamificationServiceUrl + "/api/exp/add";

            Map<String, Object> request = new HashMap<>();
            request.put("userId", userId.toString());
            request.put("exp", exp);
            request.put("reason", "COMMENT_CREATED");

            restTemplate.postForObject(url, request, Void.class);
        } catch (Exception e) {
            // Log error but don't fail the comment creation
            System.err.println("Failed to add EXP for user " + userId + ": " + e.getMessage());
        }
    }
}
