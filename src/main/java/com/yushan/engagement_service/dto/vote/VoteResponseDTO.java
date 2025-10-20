package com.yushan.engagement_service.dto.vote;

import lombok.Data;

@Data
public class VoteResponseDTO {
    private Integer novelId;
    private Integer voteCount;
    private Float remainedYuan;

    public VoteResponseDTO(Integer novelId, Integer updatedVoteCount, Float remainedYuan) {
        this.novelId = novelId;
        this.voteCount = updatedVoteCount;
        this.remainedYuan = remainedYuan;
    }
}