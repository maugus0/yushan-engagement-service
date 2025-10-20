package com.yushan.engagement_service.dto.review;

public class NovelRatingStatsDTO {
    private Integer novelId;
    private String novelTitle;
    private Integer totalReviews;
    private Float averageRating;

    private Integer rating5Count;
    private Integer rating4Count;
    private Integer rating3Count;
    private Integer rating2Count;
    private Integer rating1Count;

    private Float rating5Percentage;
    private Float rating4Percentage;
    private Float rating3Percentage;
    private Float rating2Percentage;
    private Float rating1Percentage;

    public Integer getNovelId() {
        return novelId;
    }

    public void setNovelId(Integer novelId) {
        this.novelId = novelId;
    }

    public String getNovelTitle() {
        return novelTitle;
    }

    public void setNovelTitle(String novelTitle) {
        this.novelTitle = novelTitle;
    }

    public Integer getTotalReviews() {
        return totalReviews;
    }

    public void setTotalReviews(Integer totalReviews) {
        this.totalReviews = totalReviews;
    }

    public Float getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(Float averageRating) {
        this.averageRating = averageRating;
    }

    public Integer getRating5Count() {
        return rating5Count;
    }

    public void setRating5Count(Integer rating5Count) {
        this.rating5Count = rating5Count;
    }

    public Integer getRating4Count() {
        return rating4Count;
    }

    public void setRating4Count(Integer rating4Count) {
        this.rating4Count = rating4Count;
    }

    public Integer getRating3Count() {
        return rating3Count;
    }

    public void setRating3Count(Integer rating3Count) {
        this.rating3Count = rating3Count;
    }

    public Integer getRating2Count() {
        return rating2Count;
    }

    public void setRating2Count(Integer rating2Count) {
        this.rating2Count = rating2Count;
    }

    public Integer getRating1Count() {
        return rating1Count;
    }

    public void setRating1Count(Integer rating1Count) {
        this.rating1Count = rating1Count;
    }

    public Float getRating5Percentage() {
        return rating5Percentage;
    }

    public void setRating5Percentage(Float rating5Percentage) {
        this.rating5Percentage = rating5Percentage;
    }

    public Float getRating4Percentage() {
        return rating4Percentage;
    }

    public void setRating4Percentage(Float rating4Percentage) {
        this.rating4Percentage = rating4Percentage;
    }

    public Float getRating3Percentage() {
        return rating3Percentage;
    }

    public void setRating3Percentage(Float rating3Percentage) {
        this.rating3Percentage = rating3Percentage;
    }

    public Float getRating2Percentage() {
        return rating2Percentage;
    }

    public void setRating2Percentage(Float rating2Percentage) {
        this.rating2Percentage = rating2Percentage;
    }

    public Float getRating1Percentage() {
        return rating1Percentage;
    }

    public void setRating1Percentage(Float rating1Percentage) {
        this.rating1Percentage = rating1Percentage;
    }
}

