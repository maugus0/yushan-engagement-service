package com.yushan.engagement_service.dto.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

/**
 * Vote created event for gamification service
 * Only contains essential fields needed for gamification
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VoteCreatedEvent {
    
    /**
     * Vote ID
     */
    private Integer voteId;
    
    /**
     * User ID who created the vote
     */
    private UUID userId;
    
    /**
     * Novel ID being voted on
     */
    private Integer novelId;
}
