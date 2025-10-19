package com.yushan.engagement_service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChapterDTO {
    private Integer id;
    private Integer novelId;
    private String title;
    private Integer chapterNumber;
    private Boolean isValid;
    private Date createTime;
    private Date updateTime;
}
