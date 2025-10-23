package com.yushan.engagement_service.service;

import com.yushan.engagement_service.client.ContentServiceClient;
import com.yushan.engagement_service.client.GamificationServiceClient;
import com.yushan.engagement_service.dao.VoteMapper;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.gamification.VoteCheckResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import com.yushan.engagement_service.dto.vote.VoteResponseDTO;
import com.yushan.engagement_service.dto.vote.VoteUserResponseDTO;
import com.yushan.engagement_service.entity.Vote;
import com.yushan.engagement_service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VoteServiceTest {

    @Mock
    private VoteMapper voteMapper;

    @Mock
    private ContentServiceClient contentServiceClient;

    @Mock
    private GamificationServiceClient gamificationServiceClient;

    @Mock
    private KafkaEventProducerService kafkaEventProducerService;

    @InjectMocks
    private VoteService voteService;

    private UUID testUserId;
    private Integer testNovelId;
    private Vote testVote;
    private NovelDetailResponseDTO testNovel;
    private VoteCheckResponseDTO testVoteCheck;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testNovelId = 1;
        
        testNovel = new NovelDetailResponseDTO();
        testNovel.setId(testNovelId);
        testNovel.setTitle("Test Novel");
        testNovel.setAuthorId(UUID.randomUUID()); // Different from testUserId

        testVote = new Vote();
        testVote.setId(1);
        testVote.setUserId(testUserId);
        testVote.setNovelId(testNovelId);
        testVote.setCreateTime(new Date());
        testVote.setUpdateTime(new Date());

        testVoteCheck = new VoteCheckResponseDTO();
        testVoteCheck.setCanVote(true);
        testVoteCheck.setMessage("Can vote");
    }

    @Test
    void createVote_WithValidData_ShouldCreateVote() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        ApiResponse<VoteCheckResponseDTO> voteCheckResponse = new ApiResponse<>();
        voteCheckResponse.setData(testVoteCheck);
        
        ApiResponse<Integer> voteCountResponse = new ApiResponse<>();
        voteCountResponse.setData(5);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(voteCheckResponse);
        when(voteMapper.insertSelective(any(Vote.class))).thenAnswer(invocation -> {
            Vote vote = invocation.getArgument(0);
            vote.setId(1);
            return 1;
        });
        when(contentServiceClient.incrementVoteCount(testNovelId)).thenReturn(new ApiResponse<>());
        when(contentServiceClient.getNovelVoteCount(testNovelId)).thenReturn(voteCountResponse);

        // Act
        VoteResponseDTO result = voteService.createVote(testNovelId, testUserId);

        // Assert
        assertNotNull(result);
        assertEquals(testNovelId, result.getNovelId());
        assertEquals(5, result.getVoteCount());
        assertTrue(result.getIsVoted());
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient).checkVoteEligibility();
        verify(voteMapper).insertSelective(any(Vote.class));
        verify(contentServiceClient).incrementVoteCount(testNovelId);
        verify(contentServiceClient).getNovelVoteCount(testNovelId);
        verify(kafkaEventProducerService).publishVoteCreatedEvent(anyInt(), eq(testUserId));
    }

    @Test
    void createVote_WithNonExistentNovel_ShouldThrowException() {
        // Arrange
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            voteService.createVote(testNovelId, testUserId);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient, never()).checkVoteEligibility();
        verify(voteMapper, never()).insertSelective(any(Vote.class));
    }

    @Test
    void createVote_WithAuthorVotingOwnNovel_ShouldThrowException() {
        // Arrange
        testNovel.setAuthorId(testUserId); // Same as testUserId
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            voteService.createVote(testNovelId, testUserId);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient, never()).checkVoteEligibility();
        verify(voteMapper, never()).insertSelective(any(Vote.class));
    }

    @Test
    void createVote_WithVoteCheckNull_ShouldThrowException() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(null);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            voteService.createVote(testNovelId, testUserId);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient).checkVoteEligibility();
        verify(voteMapper, never()).insertSelective(any(Vote.class));
    }

    @Test
    void createVote_WithVoteCheckDataNull_ShouldThrowException() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        ApiResponse<VoteCheckResponseDTO> voteCheckResponse = new ApiResponse<>();
        voteCheckResponse.setData(null);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(voteCheckResponse);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            voteService.createVote(testNovelId, testUserId);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient).checkVoteEligibility();
        verify(voteMapper, never()).insertSelective(any(Vote.class));
    }

    @Test
    void createVote_WithCannotVote_ShouldThrowException() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        testVoteCheck.setCanVote(false);
        testVoteCheck.setMessage("Not enough Yuan");
        ApiResponse<VoteCheckResponseDTO> voteCheckResponse = new ApiResponse<>();
        voteCheckResponse.setData(testVoteCheck);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(voteCheckResponse);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            voteService.createVote(testNovelId, testUserId);
        });
        
        assertEquals("Not enough Yuan", exception.getMessage());
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient).checkVoteEligibility();
        verify(voteMapper, never()).insertSelective(any(Vote.class));
    }

    @Test
    void createVote_WithCannotVoteNullMessage_ShouldThrowException() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        testVoteCheck.setCanVote(false);
        testVoteCheck.setMessage(null);
        ApiResponse<VoteCheckResponseDTO> voteCheckResponse = new ApiResponse<>();
        voteCheckResponse.setData(testVoteCheck);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(voteCheckResponse);

        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, () -> {
            voteService.createVote(testNovelId, testUserId);
        });
        
        assertEquals("Not enough Yuan to vote", exception.getMessage());
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(gamificationServiceClient).checkVoteEligibility();
        verify(voteMapper, never()).insertSelective(any(Vote.class));
    }

    @Test
    void getUserVotes_WithValidData_ShouldReturnVotes() {
        // Arrange
        int page = 0;
        int size = 10;
        long totalElements = 1L;
        
        when(voteMapper.countByUserId(testUserId)).thenReturn(totalElements);
        when(voteMapper.selectByUserIdWithPagination(testUserId, 0, size)).thenReturn(Arrays.asList(testVote));
        
        ApiResponse<List<NovelDetailResponseDTO>> novelResponse = new ApiResponse<>();
        novelResponse.setData(Arrays.asList(testNovel));
        when(contentServiceClient.getNovelsBatch(Arrays.asList(testNovelId))).thenReturn(novelResponse);

        // Act
        PageResponseDTO<VoteUserResponseDTO> result = voteService.getUserVotes(testUserId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(totalElements, result.getTotalElements());
        assertEquals(page, result.getCurrentPage());
        assertEquals(size, result.getSize());
        
        VoteUserResponseDTO voteDTO = result.getContent().get(0);
        assertEquals(testNovelId, voteDTO.getNovelId());
        assertEquals("Test Novel", voteDTO.getNovelTitle());
        assertNotNull(voteDTO.getVotedTime());
        
        verify(voteMapper).countByUserId(testUserId);
        verify(voteMapper).selectByUserIdWithPagination(testUserId, 0, size);
        verify(contentServiceClient).getNovelsBatch(Arrays.asList(testNovelId));
    }

    @Test
    void getUserVotes_WithNoVotes_ShouldReturnEmptyList() {
        // Arrange
        int page = 0;
        int size = 10;
        long totalElements = 0L;
        
        when(voteMapper.countByUserId(testUserId)).thenReturn(totalElements);

        // Act
        PageResponseDTO<VoteUserResponseDTO> result = voteService.getUserVotes(testUserId, page, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(0L, result.getTotalElements());
        assertEquals(page, result.getCurrentPage());
        assertEquals(size, result.getSize());
        
        verify(voteMapper).countByUserId(testUserId);
        verify(voteMapper, never()).selectByUserIdWithPagination(any(), anyInt(), anyInt());
        verify(contentServiceClient, never()).getNovelsBatch(any());
    }

    @Test
    void getUserVotes_WithEmptyVoteList_ShouldReturnEmptyList() {
        // Arrange
        int page = 0;
        int size = 10;
        long totalElements = 1L;
        
        when(voteMapper.countByUserId(testUserId)).thenReturn(totalElements);
        when(voteMapper.selectByUserIdWithPagination(testUserId, 0, size)).thenReturn(Collections.emptyList());

        // Act
        PageResponseDTO<VoteUserResponseDTO> result = voteService.getUserVotes(testUserId, page, size);

        // Assert
        assertNotNull(result);
        assertTrue(result.getContent().isEmpty());
        assertEquals(totalElements, result.getTotalElements());
        assertEquals(page, result.getCurrentPage());
        assertEquals(size, result.getSize());
        
        verify(voteMapper).countByUserId(testUserId);
        verify(voteMapper).selectByUserIdWithPagination(testUserId, 0, size);
        verify(contentServiceClient, never()).getNovelsBatch(any());
    }

    @Test
    void getUserVotes_WithNovelNotFound_ShouldReturnVoteWithDefaultTitle() {
        // Arrange
        int page = 0;
        int size = 10;
        long totalElements = 1L;
        
        when(voteMapper.countByUserId(testUserId)).thenReturn(totalElements);
        when(voteMapper.selectByUserIdWithPagination(testUserId, 0, size)).thenReturn(Arrays.asList(testVote));
        
        ApiResponse<List<NovelDetailResponseDTO>> novelResponse = new ApiResponse<>();
        novelResponse.setData(Collections.emptyList()); // No novels found
        when(contentServiceClient.getNovelsBatch(Arrays.asList(testNovelId))).thenReturn(novelResponse);

        // Act
        PageResponseDTO<VoteUserResponseDTO> result = voteService.getUserVotes(testUserId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        
        VoteUserResponseDTO voteDTO = result.getContent().get(0);
        assertEquals(testNovelId, voteDTO.getNovelId());
        assertEquals("Novel not found", voteDTO.getNovelTitle());
        
        verify(voteMapper).countByUserId(testUserId);
        verify(voteMapper).selectByUserIdWithPagination(testUserId, 0, size);
        verify(contentServiceClient).getNovelsBatch(Arrays.asList(testNovelId));
    }

    @Test
    void getUserVotes_WithNullNovelResponse_ShouldReturnVoteWithDefaultTitle() {
        // Arrange
        int page = 0;
        int size = 10;
        long totalElements = 1L;
        
        when(voteMapper.countByUserId(testUserId)).thenReturn(totalElements);
        when(voteMapper.selectByUserIdWithPagination(testUserId, 0, size)).thenReturn(Arrays.asList(testVote));
        
        when(contentServiceClient.getNovelsBatch(Arrays.asList(testNovelId))).thenReturn(null);

        // Act
        PageResponseDTO<VoteUserResponseDTO> result = voteService.getUserVotes(testUserId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        
        VoteUserResponseDTO voteDTO = result.getContent().get(0);
        assertEquals(testNovelId, voteDTO.getNovelId());
        assertEquals("Novel not found", voteDTO.getNovelTitle());
        
        verify(voteMapper).countByUserId(testUserId);
        verify(voteMapper).selectByUserIdWithPagination(testUserId, 0, size);
        verify(contentServiceClient).getNovelsBatch(Arrays.asList(testNovelId));
    }

    @Test
    void getUserVotes_WithPagination_ShouldCalculateCorrectOffset() {
        // Arrange
        int page = 2;
        int size = 5;
        int expectedOffset = 10; // page * size = 2 * 5
        long totalElements = 15L;
        
        when(voteMapper.countByUserId(testUserId)).thenReturn(totalElements);
        when(voteMapper.selectByUserIdWithPagination(testUserId, expectedOffset, size)).thenReturn(Arrays.asList(testVote));
        
        ApiResponse<List<NovelDetailResponseDTO>> novelResponse = new ApiResponse<>();
        novelResponse.setData(Arrays.asList(testNovel));
        when(contentServiceClient.getNovelsBatch(Arrays.asList(testNovelId))).thenReturn(novelResponse);

        // Act
        PageResponseDTO<VoteUserResponseDTO> result = voteService.getUserVotes(testUserId, page, size);

        // Assert
        assertNotNull(result);
        assertEquals(page, result.getCurrentPage());
        assertEquals(size, result.getSize());
        
        verify(voteMapper).countByUserId(testUserId);
        verify(voteMapper).selectByUserIdWithPagination(testUserId, expectedOffset, size);
    }
}