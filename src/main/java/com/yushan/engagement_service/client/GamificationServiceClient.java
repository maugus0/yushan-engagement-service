package com.yushan.engagement_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;
import java.util.UUID;

@FeignClient(name = "gamification-service", url = "${services.gamification.url:http://localhost:8085}")
public interface GamificationServiceClient {

    @PostMapping("/api/exp/add")
    void addExp(@RequestBody Map<String, Object> request);

    default void addExp(UUID userId, Float exp, String reason) {
        try {
            Map<String, Object> request = Map.of(
                "userId", userId.toString(),
                "exp", exp,
                "reason", reason
            );
            addExp(request);
        } catch (Exception e) {
            // Log error but don't fail the main operation
            System.err.println("Failed to add EXP for user " + userId + ": " + e.getMessage());
        }
    }

    // Convenience methods for common actions
    default void addExpForComment(UUID userId) {
        addExp(userId, 5.0f, "COMMENT_CREATED");
    }

    default void addExpForReview(UUID userId) {
        addExp(userId, 5.0f, "REVIEW_CREATED");
    }
}
