package com.yushan.engagement_service.dto.chapter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChapterDetailResponseDTO {
    
    private Integer id;
    private UUID uuid;
    private Integer novelId;
    private Integer chapterNumber;
    private String title;
    private String content;
    private String preview;
    private Integer wordCnt;
    private Boolean isPremium;
    private Float yuanCost;
    private Long viewCnt;
    private Boolean isValid;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publishTime;

    private UUID nextChapterUuid;
    private UUID previousChapterUuid;

    // Constructors
    public ChapterDetailResponseDTO() {}

    public ChapterDetailResponseDTO(Integer id, UUID uuid, Integer novelId, Integer chapterNumber, String title,
                                    String content, String preview, Integer wordCnt, Boolean isPremium, Float yuanCost,
                                    Long viewCnt, Boolean isValid, Date createTime, Date updateTime,
                                    Date publishTime) {
        this.id = id;
        this.uuid = uuid;
        this.novelId = novelId;
        this.chapterNumber = chapterNumber;
        this.title = title;
        this.content = content;
        this.preview = preview;
        this.wordCnt = wordCnt;
        this.isPremium = isPremium;
        this.yuanCost = yuanCost;
        this.viewCnt = viewCnt;
        this.isValid = isValid;
        this.createTime = createTime != null ? (Date) createTime.clone() : null;
        this.updateTime = updateTime != null ? (Date) updateTime.clone() : null;
        this.publishTime = publishTime != null ? (Date) publishTime.clone() : null;
    }

    // Getters and Setters
    public Integer getId() { 
        return id; 
    }
    
    public void setId(Integer id) { 
        this.id = id; 
    }

    public UUID getUuid() { 
        return uuid; 
    }
    
    public void setUuid(UUID uuid) { 
        this.uuid = uuid; 
    }

    public Integer getNovelId() { 
        return novelId; 
    }
    
    public void setNovelId(Integer novelId) { 
        this.novelId = novelId; 
    }

    public Integer getChapterNumber() { 
        return chapterNumber; 
    }
    
    public void setChapterNumber(Integer chapterNumber) { 
        this.chapterNumber = chapterNumber; 
    }

    public String getTitle() { 
        return title; 
    }
    
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getContent() { 
        return content; 
    }
    
    public void setContent(String content) { 
        this.content = content; 
    }

    public String getPreview() { 
        return preview; 
    }
    
    public void setPreview(String preview) { 
        this.preview = preview; 
    }

    public Integer getWordCnt() { 
        return wordCnt; 
    }
    
    public void setWordCnt(Integer wordCnt) { 
        this.wordCnt = wordCnt; 
    }

    public Boolean getIsPremium() { 
        return isPremium; 
    }
    
    public void setIsPremium(Boolean isPremium) { 
        this.isPremium = isPremium; 
    }

    public Float getYuanCost() { 
        return yuanCost; 
    }
    
    public void setYuanCost(Float yuanCost) { 
        this.yuanCost = yuanCost; 
    }

    public Long getViewCnt() { 
        return viewCnt; 
    }
    
    public void setViewCnt(Long viewCnt) { 
        this.viewCnt = viewCnt; 
    }

    public Boolean getIsValid() { 
        return isValid; 
    }
    
    public void setIsValid(Boolean isValid) { 
        this.isValid = isValid; 
    }

    public Date getCreateTime() { 
        return createTime != null ? (Date) createTime.clone() : null; 
    }
    
    public void setCreateTime(Date createTime) { 
        this.createTime = createTime != null ? (Date) createTime.clone() : null; 
    }

    public Date getUpdateTime() { 
        return updateTime != null ? (Date) updateTime.clone() : null; 
    }
    
    public void setUpdateTime(Date updateTime) { 
        this.updateTime = updateTime != null ? (Date) updateTime.clone() : null; 
    }

    public Date getPublishTime() { 
        return publishTime != null ? (Date) publishTime.clone() : null; 
    }
    
    public void setPublishTime(Date publishTime) { 
        this.publishTime = publishTime != null ? (Date) publishTime.clone() : null; 
    }

    public UUID getNextChapterUuid() { 
        return nextChapterUuid; 
    }
    
    public void setNextChapterUuid(UUID nextChapterUuid) { 
        this.nextChapterUuid = nextChapterUuid; 
    }

    public UUID getPreviousChapterUuid() { 
        return previousChapterUuid; 
    }
    
    public void setPreviousChapterUuid(UUID previousChapterUuid) { 
        this.previousChapterUuid = previousChapterUuid; 
    }
}
