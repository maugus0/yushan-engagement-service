package com.yushan.engagement_service.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
public class ChapterDetailResponseDTO {
    private Integer id;
    private Integer novelId;
    private String title;
    private Integer chapterNumber;
    private Boolean isValid;
    private Date createTime;
    private Date updateTime;

    public ChapterDetailResponseDTO(Integer id, Integer novelId, String title, Integer chapterNumber, Boolean isValid, Date createTime, Date updateTime) {
        this.id = id;
        this.novelId = novelId;
        this.title = title;
        this.chapterNumber = chapterNumber;
        this.isValid = isValid;
        this.createTime = createTime != null ? new Date(createTime.getTime()) : null;
        this.updateTime = updateTime != null ? new Date(updateTime.getTime()) : null;
    }

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

    public static ChapterDetailResponseDTOBuilder builder() {
        return new ChapterDetailResponseDTOBuilder();
    }

    public static class ChapterDetailResponseDTOBuilder {
        private Integer id;
        private Integer novelId;
        private String title;
        private Integer chapterNumber;
        private Boolean isValid;
        private Date createTime;
        private Date updateTime;

        public ChapterDetailResponseDTOBuilder createTime(Date createTime) {
            this.createTime = createTime != null ? new Date(createTime.getTime()) : null;
            return this;
        }

        public ChapterDetailResponseDTOBuilder updateTime(Date updateTime) {
            this.updateTime = updateTime != null ? new Date(updateTime.getTime()) : null;
            return this;
        }

        public ChapterDetailResponseDTO build() {
            return new ChapterDetailResponseDTO(id, novelId, title, chapterNumber, isValid, createTime, updateTime);
        }
    }
}
