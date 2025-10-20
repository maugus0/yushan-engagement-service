package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.user.UserResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.Objects;
import java.util.UUID;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.user.url:http://localhost:8081}")
    private String userServiceUrl;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = Objects.requireNonNull(restTemplate, "RestTemplate cannot be null");
    }

    public UserResponseDTO getUser(UUID userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            return restTemplate.getForObject(url, UserResponseDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user from user service", e);
        }
    }

    public String getUsernameById(UUID userId) {
        try {
            UserResponseDTO user = getUser(userId);
            return user != null ? user.getUsername() : "Unknown User";
        } catch (Exception e) {
            return "Unknown User";
        }
    }
}
