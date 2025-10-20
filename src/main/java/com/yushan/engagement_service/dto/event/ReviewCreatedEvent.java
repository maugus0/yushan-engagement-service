package com.yushan.engagement_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Review created event published when a user creates a review
 * 
 * Consumed by:
 * - Analytics Service: Track user engagement metrics
 * - Gamification Service: Award points for reviewing
 * - Content Service: Update review counts and ratings
 * - User Service: Update user activity feeds
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreatedEvent {
    
    /**
     * Event type identifier
     */
    @Builder.Default
    private String eventType = "REVIEW_CREATED";
    
    /**
     * Review ID
     */
    private Integer reviewId;
    
    /**
     * Review UUID
     */
    private UUID reviewUuid;
    
    /**
     * User ID who created the review
     */
    private UUID userId;
    
    /**
     * Novel ID being reviewed
     */
    private Integer novelId;
    
    /**
     * Rating given (1-5)
     */
    private Integer rating;
    
    /**
     * Review title
     */
    private String title;
    
    /**
     * Review content length
     */
    private Integer contentLength;
    
    /**
     * Is spoiler review
     */
    private Boolean isSpoiler;
    
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
