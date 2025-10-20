package com.yushan.engagement_service.service;

import com.yushan.engagement_service.dao.VoteMapper;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.vote.VoteResponseDTO;
import com.yushan.engagement_service.dto.vote.VoteUserResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import com.yushan.engagement_service.entity.Vote;
import com.yushan.engagement_service.client.ContentServiceClient;
import com.yushan.engagement_service.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class VoteService {

    @Autowired
    private VoteMapper voteMapper;

    @Autowired
    private ContentServiceClient contentServiceClient;

    @Autowired
    private KafkaEventProducerService kafkaEventProducerService;

    /**
     * Toggle vote for a novel (vote if not voted, unvote if already voted)
     */
    @Transactional
    public VoteResponseDTO toggleVote(Integer novelId, UUID userId) {
        // Check novel exists via content service
        if (!contentServiceClient.novelExists(novelId)) {
            throw new ValidationException("Novel does not exist: " + novelId);
        }

        // Check if user already voted for this novel
        Vote existingVote = voteMapper.selectByUserAndNovel(userId, novelId);
        
        if (existingVote != null) {
            // User already voted, so unvote (delete the vote)
            voteMapper.deleteByPrimaryKey(existingVote.getId());
            
            // Get updated vote count
            long voteCount = voteMapper.countByUserId(userId);
            return new VoteResponseDTO(novelId, (int)voteCount, false); // false = unvoted
        } else {
            // User hasn't voted, so vote (create new vote)
            Vote vote = new Vote();
            vote.setUserId(userId);
            vote.setNovelId(novelId);
            Date now = new Date();
            vote.setCreateTime(now);
            vote.setUpdateTime(now);

            voteMapper.insertSelective(vote);

            // Get updated vote count
            long voteCount = voteMapper.countByUserId(userId);
            return new VoteResponseDTO(novelId, (int)voteCount, true); // true = voted
        }
    }

    public PageResponseDTO<VoteUserResponseDTO> getUserVotes(UUID userId, int page, int size) {
        int offset = page * size;
        long totalElements = voteMapper.countByUserId(userId);

        List<Vote> votes = voteMapper.selectByUserIdWithPagination(userId, offset, size);

        List<VoteUserResponseDTO> dtos = new ArrayList<>();
        for (Vote vote : votes) {
            VoteUserResponseDTO dto = new VoteUserResponseDTO();
            dto.setId(vote.getId());
            dto.setNovelId(vote.getNovelId());
            
            // Fetch novel title from ContentService
            try {
                ApiResponse<NovelDetailResponseDTO> novelResponse = contentServiceClient.getNovelById(vote.getNovelId());
                dto.setNovelTitle(novelResponse != null && novelResponse.getData() != null ? novelResponse.getData().getTitle() : "Unknown Novel");
            } catch (Exception e) {
                dto.setNovelTitle("Unknown Novel");
            }
            
            dto.setVotedTime(convertToLocalDateTime(vote.getCreateTime()));
            dtos.add(dto);
        }

        return new PageResponseDTO<>(dtos, totalElements, page, size);
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}