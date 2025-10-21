package com.yushan.engagement_service.dto.gamification;

public class VoteCheckResponseDTO {
    private boolean canVote;
    private double currentYuanBalance;
    private double requiredYuan;
    private String message;

    public VoteCheckResponseDTO() {}

    public VoteCheckResponseDTO(boolean canVote, double currentYuanBalance, double requiredYuan, String message) {
        this.canVote = canVote;
        this.currentYuanBalance = currentYuanBalance;
        this.requiredYuan = requiredYuan;
        this.message = message;
    }

    // Getters and Setters
    public boolean isCanVote() { return canVote; }
    public void setCanVote(boolean canVote) { this.canVote = canVote; }
    public double getCurrentYuanBalance() { return currentYuanBalance; }
    public void setCurrentYuanBalance(double currentYuanBalance) { this.currentYuanBalance = currentYuanBalance; }
    public double getRequiredYuan() { return requiredYuan; }
    public void setRequiredYuan(double requiredYuan) { this.requiredYuan = requiredYuan; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
