package com.yushan.engagement_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Comment created event published when a user creates a comment
 * 
 * Consumed by:
 * - Analytics Service: Track user engagement metrics
 * - Gamification Service: Award points for commenting
 * - Content Service: Update comment counts
 * - User Service: Update user activity feeds
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedEvent {
    
    /**
     * Event type identifier
     */
    @Builder.Default
    private String eventType = "COMMENT_CREATED";
    
    /**
     * Comment ID
     */
    private Integer commentId;
    
    /**
     * User ID who created the comment
     */
    private UUID userId;
    
    /**
     * Chapter ID where comment was made
     */
    private Integer chapterId;
    
    /**
     * Novel ID (derived from chapter)
     */
    private Integer novelId;
    
    /**
     * Comment content length
     */
    private Integer contentLength;
    
    /**
     * Is spoiler comment
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
