package com.yushan.engagement_service.client;

import com.yushan.engagement_service.config.FeignAuthConfig;
import com.yushan.engagement_service.dto.user.UserResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${services.user.url:http://yushan-user-service:8081}", 
            configuration = FeignAuthConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/users/{userId}")
    UserResponseDTO getUser(@PathVariable("userId") UUID userId);

    default String getUsernameById(UUID userId) {
        try {
            UserResponseDTO user = getUser(userId);
            return user != null ? user.getUsername() : "Unknown User";
        } catch (Exception e) {
            return "Unknown User";
        }
    }
}
