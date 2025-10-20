package com.yushan.engagement_service.dto.novel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import java.util.Date;
import java.util.UUID;

/**
 * DTO for mapping novel details from content-service.
 * Field names and types are aligned with content service to ensure compatibility.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class NovelDetailResponseDTO {
    // Primary identifiers
    private Integer id;
    private UUID uuid;
    private String title;

    // Author information
    private UUID authorId;
    private String authorUsername;

    // Category information
    private Integer categoryId;
    private String categoryName;

    // Content information
    private String synopsis;
    private String coverImgUrl;
    private String status;
    private Boolean isCompleted;

    // Statistics
    private Integer chapterCnt;
    private Long wordCnt;
    private Float avgRating;
    private Integer reviewCnt;
    private Long viewCnt;
    private Integer voteCnt;
    private Float yuanCnt;

    // Timestamps
    private Date createTime;
    private Date updateTime;
    private Date publishTime;
}
