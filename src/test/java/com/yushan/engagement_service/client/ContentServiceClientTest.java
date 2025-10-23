package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.chapter.ChapterDetailResponseDTO;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for ContentServiceClient
 */
@ExtendWith(MockitoExtension.class)
class ContentServiceClientTest {

    @Mock
    private ContentServiceClient contentServiceClient;

    @BeforeEach
    void setUp() {
        // Reset mocks before each test
        reset(contentServiceClient);
    }

    @Test
    void getChaptersBatch_Success() {
        // Arrange
        List<Integer> chapterIds = List.of(1, 2);
        List<ChapterDetailResponseDTO> chapters = List.of(
            createChapterDTO(1, "Chapter 1"),
            createChapterDTO(2, "Chapter 2")
        );
        ApiResponse<List<ChapterDetailResponseDTO>> response = ApiResponse.success(chapters);
        
        when(contentServiceClient.getChaptersBatch(chapterIds)).thenReturn(response);

        // Act
        ApiResponse<List<ChapterDetailResponseDTO>> result = contentServiceClient.getChaptersBatch(chapterIds);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("Chapter 1", result.getData().get(0).getTitle());
        assertEquals("Chapter 2", result.getData().get(1).getTitle());
        verify(contentServiceClient).getChaptersBatch(chapterIds);
    }

    @Test
    void getNovelsBatch_Success() {
        // Arrange
        List<Integer> novelIds = List.of(1, 2);
        List<NovelDetailResponseDTO> novels = List.of(
            createNovelDTO(1, "Novel 1"),
            createNovelDTO(2, "Novel 2")
        );
        ApiResponse<List<NovelDetailResponseDTO>> response = ApiResponse.success(novels);
        
        when(contentServiceClient.getNovelsBatch(novelIds)).thenReturn(response);

        // Act
        ApiResponse<List<NovelDetailResponseDTO>> result = contentServiceClient.getNovelsBatch(novelIds);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(2, result.getData().size());
        assertEquals("Novel 1", result.getData().get(0).getTitle());
        assertEquals("Novel 2", result.getData().get(1).getTitle());
        verify(contentServiceClient).getNovelsBatch(novelIds);
    }

    @Test
    void getNovelById_Success() {
        // Arrange
        NovelDetailResponseDTO novel = createNovelDTO(1, "Test Novel");
        ApiResponse<NovelDetailResponseDTO> response = ApiResponse.success(novel);
        
        when(contentServiceClient.getNovelById(1)).thenReturn(response);

        // Act
        ApiResponse<NovelDetailResponseDTO> result = contentServiceClient.getNovelById(1);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(1, result.getData().getId());
        assertEquals("Test Novel", result.getData().getTitle());
        verify(contentServiceClient).getNovelById(1);
    }

    @Test
    void getNovelVoteCount_Success() {
        // Arrange
        ApiResponse<Integer> response = ApiResponse.success(100);
        
        when(contentServiceClient.getNovelVoteCount(1)).thenReturn(response);

        // Act
        ApiResponse<Integer> result = contentServiceClient.getNovelVoteCount(1);

        // Assert
        assertNotNull(result);
        assertEquals(100, result.getData());
        verify(contentServiceClient).getNovelVoteCount(1);
    }

    @Test
    void getChaptersByNovelId_Success() {
        // Arrange
        List<ChapterDetailResponseDTO> chapters = List.of(
            createChapterDTO(1, "Chapter 1"),
            createChapterDTO(2, "Chapter 2")
        );
        PageResponseDTO<ChapterDetailResponseDTO> pageResponse = new PageResponseDTO<>();
        pageResponse.setContent(chapters);
        pageResponse.setCurrentPage(0);
        pageResponse.setSize(10);
        pageResponse.setTotalElements(2L);
        pageResponse.setTotalPages(1);

        ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> response = ApiResponse.success(pageResponse);
        
        when(contentServiceClient.getChaptersByNovelId(1)).thenReturn(response);

        // Act
        ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> result = contentServiceClient.getChaptersByNovelId(1);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(2, result.getData().getContent().size());
        assertEquals("Chapter 1", result.getData().getContent().get(0).getTitle());
        assertEquals("Chapter 2", result.getData().getContent().get(1).getTitle());
        verify(contentServiceClient).getChaptersByNovelId(1);
    }

    @Test
    void getNovelByIdRaw_Success() {
        // Arrange
        ApiResponse<java.util.Map<String, Object>> response = ApiResponse.success(new java.util.HashMap<>());
        
        when(contentServiceClient.getNovelByIdRaw(1)).thenReturn(response);

        // Act
        ApiResponse<java.util.Map<String, Object>> result = contentServiceClient.getNovelByIdRaw(1);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getData());
        verify(contentServiceClient).getNovelByIdRaw(1);
    }

    @Test
    void headlessGetNovelById_Success() {
        // Arrange
        feign.Response response = feign.Response.builder()
            .status(200)
            .reason("OK")
            .request(feign.Request.create(feign.Request.HttpMethod.GET, "/api/v1/novels/1", 
                java.util.Collections.emptyMap(), null, feign.Util.UTF_8))
            .build();
        
        when(contentServiceClient.headlessGetNovelById(1)).thenReturn(response);

        // Act
        feign.Response result = contentServiceClient.headlessGetNovelById(1);

        // Assert
        assertNotNull(result);
        assertEquals(200, result.status());
        verify(contentServiceClient).headlessGetNovelById(1);
    }

    // Helper methods
    private ChapterDetailResponseDTO createChapterDTO(Integer id, String title) {
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();
        dto.setId(id);
        dto.setUuid(UUID.randomUUID());
        dto.setNovelId(1);
        dto.setChapterNumber(id);
        dto.setTitle(title);
        dto.setContent("Content for " + title);
        dto.setPreview("Preview for " + title);
        dto.setWordCnt(1000);
        dto.setIsPremium(false);
        dto.setYuanCost(0.0f);
        dto.setViewCnt(100L);
        dto.setIsValid(true);
        dto.setCreateTime(new Date());
        dto.setUpdateTime(new Date());
        dto.setPublishTime(new Date());
        return dto;
    }

    private NovelDetailResponseDTO createNovelDTO(Integer id, String title) {
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        dto.setId(id);
        dto.setUuid(UUID.randomUUID());
        dto.setTitle(title);
        dto.setAuthorId(UUID.randomUUID());
        dto.setSynopsis("Synopsis for " + title);
        dto.setCoverImgUrl("cover" + id + ".jpg");
        dto.setStatus("PUBLISHED");
        dto.setIsCompleted(false);
        dto.setChapterCnt(10);
        dto.setWordCnt(100000L);
        dto.setViewCnt(1000L);
        dto.setVoteCnt(100);
        dto.setAvgRating(4.5f);
        dto.setReviewCnt(50);
        dto.setCreateTime(new Date());
        dto.setUpdateTime(new Date());
        dto.setPublishTime(new Date());
        return dto;
    }
}
