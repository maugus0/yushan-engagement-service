package com.yushan.engagement_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Review created event for gamification service
 * Only contains essential fields needed for gamification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewCreatedEvent {
    
    /**
     * Review ID
     */
    private Integer reviewId;
    
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
}
