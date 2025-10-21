package com.yushan.engagement_service.client;

import com.yushan.engagement_service.config.FeignAuthConfig;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.gamification.VoteCheckResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "gamification-service", url = "${services.gamification.url:http://yushan-gamification-service:8085}", 
            configuration = FeignAuthConfig.class)
public interface GamificationServiceClient {

    @GetMapping("/api/v1/gamification/votes/check")
    ApiResponse<VoteCheckResponseDTO> checkVoteEligibility();
}
