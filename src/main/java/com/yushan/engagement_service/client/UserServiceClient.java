package com.yushan.engagement_service.client;

import com.yushan.engagement_service.config.FeignAuthConfig;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.user.UserProfileResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service", url = "${services.user.url:http://yushan-user-service:8081}", 
            configuration = FeignAuthConfig.class)
public interface UserServiceClient {

    @GetMapping("/api/v1/users/{userId}")
    ApiResponse<UserProfileResponseDTO> getUser(@PathVariable("userId") UUID userId);

    default String getUsernameById(UUID userId) {
        try {
            ApiResponse<UserProfileResponseDTO> response = getUser(userId);
            if (response != null && response.getData() != null) {
                return response.getData().getUsername();
            }
            return "Unknown User";
        } catch (Exception e) {
            return "Unknown User";
        }
    }
}
