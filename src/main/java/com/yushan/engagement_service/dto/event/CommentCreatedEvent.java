package com.yushan.engagement_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Comment created event for gamification service
 * Only contains essential fields needed for gamification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentCreatedEvent {
    
    /**
     * Comment ID
     */
    private Integer commentId;
    
    /**
     * User ID who created the comment
     */
    private UUID userId;
    
    /**
     * Novel ID being commented on
     */
    private Integer novelId;
}
