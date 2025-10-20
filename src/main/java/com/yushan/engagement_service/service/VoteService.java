package com.yushan.engagement_service.service;

import com.yushan.engagement_service.dao.VoteMapper;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.vote.VoteResponseDTO;
import com.yushan.engagement_service.dto.vote.VoteUserResponseDTO;
import com.yushan.engagement_service.entity.Vote;
import com.yushan.engagement_service.client.ContentServiceClient;
import com.yushan.engagement_service.client.UserServiceClient;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import com.yushan.engagement_service.dto.user.UserResponseDTO;
import com.yushan.engagement_service.exception.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
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
    private UserServiceClient userServiceClient;

    @Autowired
    private KafkaEventProducerService kafkaEventProducerService;

    /**
     * Create vote for a novel
     */
    @Transactional
    public VoteResponseDTO createVote(Integer novelId, UUID userId) {
        // Validate novel exists and get authorId via content service
        ApiResponse<NovelDetailResponseDTO> novelResp = contentServiceClient.getNovelById(novelId);
        if (novelResp == null || novelResp.getData() == null) {
            throw new ValidationException("Novel does not exist: " + novelId);
        }
        NovelDetailResponseDTO novel = novelResp.getData();

        // Author cannot vote own novel
        if (novel.getAuthorId() != null && novel.getAuthorId().equals(userId)) {
            throw new ValidationException("Cannot vote your own novel");
        }
        // Load user and check yuan >= 1
        // TODO: userServiceClient
        // UserResponseDTO user = userServiceClient.getUser(userId);
        // if (user == null) {
        //     throw new ValidationException("User not found");
        // }
        // NOTE: engagement UserResponseDTO lacks yuan; if needed, extend DTO to include yuan
        // TODO
        // if (user.getYuan() < 1) {
        //     throw new ValidationException("Not enough yuan");
        // }

        // Create vote (no toggle per backend logic; always create and charge 1 yuan)
        Vote vote = new Vote();
        vote.setUserId(userId);
        vote.setNovelId(novelId);
        Date now = new Date();
        vote.setCreateTime(now);
        vote.setUpdateTime(now);
        voteMapper.insertSelective(vote);

        // // update yuan: TODO: userServiceClient
        // user.setYuan(user.getYuan() - 1);
        // userMapper.updateByPrimaryKeySelective(user);

        // Update novel vote count
        contentServiceClient.incrementVoteCount(novelId);
        // Get updated vote count
        ApiResponse<Integer> voteCountResponse = contentServiceClient.getNovelVoteCount(novelId);
        Integer updatedVoteCount = voteCountResponse != null && voteCountResponse.getData() != null 
            ? voteCountResponse.getData() : 0;

        // Publish Kafka event for gamification
        kafkaEventProducerService.publishVoteCreatedEvent(
                vote.getId(),
                userId,
                novelId
        );

        return new VoteResponseDTO(novelId, updatedVoteCount, true);
    }

    public PageResponseDTO<VoteUserResponseDTO> getUserVotes(UUID userId, int page, int size) {
        int offset = page * size;
        long totalElements = voteMapper.countByUserId(userId);

        if (totalElements == 0) {
            return new PageResponseDTO<>(Collections.emptyList(), 0L, page, size);
        }

        List<Vote> votes = voteMapper.selectByUserIdWithPagination(userId, offset, size);
        if (votes.isEmpty()) {
            return new PageResponseDTO<>(Collections.emptyList(), totalElements, page, size);
        }

        List<Integer> novelIds = votes.stream()
                .map(Vote::getNovelId)
                .distinct()
                .collect(Collectors.toList());

        // Get novels from content service
        ApiResponse<List<NovelDetailResponseDTO>> novelResponse = contentServiceClient.getNovelsBatch(novelIds);
        final Map<Integer, NovelDetailResponseDTO> novelMap;
        if (novelResponse != null && novelResponse.getData() != null) {
            novelMap = novelResponse.getData().stream()
                    .collect(Collectors.toMap(NovelDetailResponseDTO::getId, novel -> novel));
        } else {
            novelMap = new HashMap<>();
        }

        List<VoteUserResponseDTO> dtos = votes.stream()
                .map(vote -> {
                    NovelDetailResponseDTO novel = novelMap.get(vote.getNovelId());
                    return convertToDTO(vote, novel);
                })
                .collect(Collectors.toList());
        return new PageResponseDTO<>(dtos, totalElements, page, size);
    }

    private VoteUserResponseDTO convertToDTO(Vote vote, NovelDetailResponseDTO novel) {
        VoteUserResponseDTO dto = new VoteUserResponseDTO();
        dto.setId(vote.getId());
        dto.setNovelId(vote.getNovelId());
        dto.setNovelTitle(novel != null ? novel.getTitle() : "Novel not found");
        dto.setVotedTime(convertToLocalDateTime(vote.getCreateTime()));

        return dto;
    }

    private LocalDateTime convertToLocalDateTime(Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
}