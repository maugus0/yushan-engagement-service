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

    // Defensive copy methods to prevent internal representation exposure
    public Date getCreateTime() {
        return createTime != null ? new Date(createTime.getTime()) : null;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime != null ? new Date(createTime.getTime()) : null;
    }

    public Date getUpdateTime() {
        return updateTime != null ? new Date(updateTime.getTime()) : null;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime != null ? new Date(updateTime.getTime()) : null;
    }

    public Date getPublishTime() {
        return publishTime != null ? new Date(publishTime.getTime()) : null;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime != null ? new Date(publishTime.getTime()) : null;
    }
}
