package com.yushan.engagement_service.dto.vote;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VoteResponseDTO {
    private Integer novelId;
    private Integer voteCount;
    private Boolean isVoted; // true = user voted, false = user unvoted
    private Float remainedYuan;

    public VoteResponseDTO(Integer novelId, Integer voteCount, Boolean isVoted) {
        this.novelId = novelId;
        this.voteCount = voteCount;
        this.isVoted = isVoted;
        this.remainedYuan = null;
    }
}