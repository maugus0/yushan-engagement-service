package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.dto.vote.*;
import com.yushan.engagement_service.dto.common.*;
import com.yushan.engagement_service.exception.ValidationException;
import com.yushan.engagement_service.security.CustomUserDetails;
import com.yushan.engagement_service.service.VoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/votes")
@Tag(name = "Vote Management", description = "APIs for managing votes")
public class VoteController {

    @Autowired
    private VoteService voteService;

    /**
     * Create a vote for a novel
     */
    @PostMapping("/novels/{novelId}")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[USER] Create vote", description = "Vote a novel. A user can vote multiple times for the same novel.")
    public ApiResponse<VoteResponseDTO> createVote(@PathVariable Integer novelId,
                                                   Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        VoteResponseDTO response = voteService.createVote(novelId, userId);

        return ApiResponse.success("Vote created successfully", response);
    }

    /**
     * Get a user's all vote record
     */
    @GetMapping("/users")
    @PreAuthorize("isAuthenticated()")
    @Operation(summary = "[USER] Get user votes", description = "Retrieve the current user's vote history.")
    public ApiResponse<PageResponseDTO<VoteUserResponseDTO>> getUserVotes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        PageResponseDTO<VoteUserResponseDTO> response = voteService.getUserVotes(userId, page, size);
        return ApiResponse.success("User votes retrieved", response);
    }

    /**
     * Extract user ID from authentication
     */
    private UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            if (userDetails.getUserId() != null) {
                return UUID.fromString(userDetails.getUserId());
            }
        }
        throw new ValidationException("User not authenticated or user ID not found.");
    }
}