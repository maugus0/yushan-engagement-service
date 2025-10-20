package com.yushan.engagement_service.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO for novel information in engagement service.
 * Contains basic novel information needed for engagement operations.
 */
@Data
public class NovelDTO {
    // Primary identifiers
    private Integer id;
    private String title;
    private String description;
    
    // Author information
    private UUID authorId;
    private String authorName;
    
    // Category information
    private Integer categoryId;
    private String categoryName;
    
    // Content information
    private String coverImageUrl;
    private Integer status;
    private Boolean isCompleted;
    
    // Statistics
    private Integer totalChapters;
    private Integer totalViews;
    private Double averageRating;
    private Integer totalRatings;
    
    // Timestamps
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
