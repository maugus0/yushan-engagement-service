package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.UserDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.HttpClientErrorException;

import java.util.UUID;

@Component
public class UserServiceClient {

    private final RestTemplate restTemplate;

    @Value("${services.user.url:http://localhost:8082}")
    private String userServiceUrl;

    public UserServiceClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public UserDTO getUser(UUID userId) {
        try {
            String url = userServiceUrl + "/api/users/" + userId;
            return restTemplate.getForObject(url, UserDTO.class);
        } catch (HttpClientErrorException.NotFound e) {
            return null;
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch user from user service", e);
        }
    }

    public String getUsernameById(UUID userId) {
        try {
            UserDTO user = getUser(userId);
            return user != null ? user.getUsername() : "Unknown User";
        } catch (Exception e) {
            return "Unknown User";
        }
    }
}
