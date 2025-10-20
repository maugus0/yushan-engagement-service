package com.yushan.engagement_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Vote created event published when a user votes for a novel
 * 
 * Consumed by:
 * - Analytics Service: Track user engagement metrics
 * - Gamification Service: Award points for voting
 * - Content Service: Update vote counts
 * - User Service: Update user activity feeds
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteCreatedEvent {
    
    /**
     * Event type identifier
     */
    @Builder.Default
    private String eventType = "VOTE_CREATED";
    
    /**
     * Vote ID
     */
    private Integer voteId;
    
    /**
     * User ID who created the vote
     */
    private UUID userId;
    
    /**
     * Novel ID being voted for
     */
    private Integer novelId;
    
    /**
     * Event timestamp
     */
    private LocalDateTime timestamp;
    
    /**
     * Service that published the event
     */
    @Builder.Default
    private String serviceName = "engagement-service";
    
    /**
     * Event version for schema evolution
     */
    @Builder.Default
    private String eventVersion = "1.0";
}
