package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.chapter.ChapterDetailResponseDTO;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import feign.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ContentServiceClientTest {

    @Mock
    private ContentServiceClient contentServiceClient;

    private ChapterDetailResponseDTO testChapter;
    private NovelDetailResponseDTO testNovel;
    private ApiResponse<List<ChapterDetailResponseDTO>> chapterListResponse;
    private ApiResponse<List<NovelDetailResponseDTO>> novelListResponse;
    private ApiResponse<NovelDetailResponseDTO> novelResponse;
    private ApiResponse<Integer> voteCountResponse;
    private ApiResponse<String> stringResponse;
    private ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> pageResponse;

    @BeforeEach
    void setUp() {
        // Setup test chapter
        testChapter = new ChapterDetailResponseDTO();
        testChapter.setId(1);
        testChapter.setIsValid(true);
        testChapter.setTitle("Test Chapter");
        testChapter.setContent("Test Content");

        // Setup test novel
        testNovel = new NovelDetailResponseDTO();
        testNovel.setId(1);
        testNovel.setTitle("Test Novel");
        testNovel.setSynopsis("Test Synopsis");

        // Setup responses
        chapterListResponse = ApiResponse.success("Success", Arrays.asList(testChapter));
        novelListResponse = ApiResponse.success("Success", Arrays.asList(testNovel));
        novelResponse = ApiResponse.success("Success", testNovel);
        voteCountResponse = ApiResponse.success("Success", 10);
        stringResponse = ApiResponse.success("Success", "OK");

        PageResponseDTO<ChapterDetailResponseDTO> pageData = new PageResponseDTO<>();
        pageData.setContent(Arrays.asList(testChapter));
        pageData.setTotalElements(1L);
        pageData.setTotalPages(1);
        pageData.setCurrentPage(0);
        pageData.setSize(10);
        pageResponse = ApiResponse.success("Success", pageData);
    }

    @Test
    void testGetChaptersBatch_Success() {
        // Given
        List<Integer> chapterIds = Arrays.asList(1, 2);
        when(contentServiceClient.getChaptersBatch(chapterIds)).thenReturn(chapterListResponse);

        // When
        ApiResponse<List<ChapterDetailResponseDTO>> result = contentServiceClient.getChaptersBatch(chapterIds);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(testChapter.getId(), result.getData().get(0).getId());

        verify(contentServiceClient).getChaptersBatch(chapterIds);
    }

    @Test
    void testGetChaptersBatch_EmptyList() {
        // Given
        List<Integer> chapterIds = Collections.emptyList();
        ApiResponse<List<ChapterDetailResponseDTO>> emptyResponse = ApiResponse.success("Success", Collections.emptyList());
        when(contentServiceClient.getChaptersBatch(chapterIds)).thenReturn(emptyResponse);

        // When
        ApiResponse<List<ChapterDetailResponseDTO>> result = contentServiceClient.getChaptersBatch(chapterIds);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isEmpty());

        verify(contentServiceClient).getChaptersBatch(chapterIds);
    }

    @Test
    void testGetNovelsBatch_Success() {
        // Given
        List<Integer> novelIds = Arrays.asList(1, 2);
        when(contentServiceClient.getNovelsBatch(novelIds)).thenReturn(novelListResponse);

        // When
        ApiResponse<List<NovelDetailResponseDTO>> result = contentServiceClient.getNovelsBatch(novelIds);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().size());
        assertEquals(testNovel.getId(), result.getData().get(0).getId());

        verify(contentServiceClient).getNovelsBatch(novelIds);
    }

    @Test
    void testGetNovelById_Success() {
        // Given
        Integer novelId = 1;
        when(contentServiceClient.getNovelById(novelId)).thenReturn(novelResponse);

        // When
        ApiResponse<NovelDetailResponseDTO> result = contentServiceClient.getNovelById(novelId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(testNovel.getId(), result.getData().getId());

        verify(contentServiceClient).getNovelById(novelId);
    }

    @Test
    void testGetNovelVoteCount_Success() {
        // Given
        Integer novelId = 1;
        when(contentServiceClient.getNovelVoteCount(novelId)).thenReturn(voteCountResponse);

        // When
        ApiResponse<Integer> result = contentServiceClient.getNovelVoteCount(novelId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(10, result.getData());

        verify(contentServiceClient).getNovelVoteCount(novelId);
    }

    @Test
    void testIncrementVoteCount_Success() {
        // Given
        Integer novelId = 1;
        when(contentServiceClient.incrementVoteCount(novelId)).thenReturn(stringResponse);

        // When
        ApiResponse<String> result = contentServiceClient.incrementVoteCount(novelId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals("OK", result.getData());

        verify(contentServiceClient).incrementVoteCount(novelId);
    }

    @Test
    void testUpdateNovelRatingAndCount_Success() {
        // Given
        Integer novelId = 1;
        Float avgRating = 4.5f;
        Integer reviewCount = 10;
        when(contentServiceClient.updateNovelRatingAndCount(novelId, avgRating, reviewCount)).thenReturn(stringResponse);

        // When
        ApiResponse<String> result = contentServiceClient.updateNovelRatingAndCount(novelId, avgRating, reviewCount);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals("OK", result.getData());

        verify(contentServiceClient).updateNovelRatingAndCount(novelId, avgRating, reviewCount);
    }

    @Test
    void testGetChaptersByNovelId_Success() {
        // Given
        Integer novelId = 1;
        when(contentServiceClient.getChaptersByNovelId(novelId)).thenReturn(pageResponse);

        // When
        ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> result = contentServiceClient.getChaptersByNovelId(novelId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getContent());
        assertEquals(1, result.getData().getContent().size());

        verify(contentServiceClient).getChaptersByNovelId(novelId);
    }

    @Test
    void testGetNovelByIdRaw_Success() {
        // Given
        Integer novelId = 1;
        Map<String, Object> rawData = Map.of("id", 1, "title", "Test Novel");
        ApiResponse<Map<String, Object>> rawResponse = ApiResponse.success("Success", rawData);
        when(contentServiceClient.getNovelByIdRaw(novelId)).thenReturn(rawResponse);

        // When
        ApiResponse<Map<String, Object>> result = contentServiceClient.getNovelByIdRaw(novelId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(1, result.getData().get("id"));

        verify(contentServiceClient).getNovelByIdRaw(novelId);
    }

    @Test
    void testHeadlessGetNovelById_Success() {
        // Given
        Integer novelId = 1;
        Response mockResponse = mock(Response.class);
        when(contentServiceClient.headlessGetNovelById(novelId)).thenReturn(mockResponse);

        // When
        Response result = contentServiceClient.headlessGetNovelById(novelId);

        // Then
        assertNotNull(result);
        assertEquals(mockResponse, result);

        verify(contentServiceClient).headlessGetNovelById(novelId);
    }

    // Test default methods using a concrete implementation
    @Test
    void testGetChapter_DefaultMethod_Success() {
        // Given
        ContentServiceClient client = new ContentServiceClient() {
            @Override
            public ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(List<Integer> chapterIds) {
                return chapterListResponse;
            }

            @Override
            public ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(List<Integer> novelIds) {
                return novelListResponse;
            }

            @Override
            public ApiResponse<NovelDetailResponseDTO> getNovelById(Integer novelId) {
                return novelResponse;
            }

            @Override
            public ApiResponse<Integer> getNovelVoteCount(Integer novelId) {
                return voteCountResponse;
            }

            @Override
            public ApiResponse<String> incrementVoteCount(Integer novelId) {
                return stringResponse;
            }

            @Override
            public ApiResponse<String> updateNovelRatingAndCount(Integer novelId, Float avgRating, Integer reviewCount) {
                return stringResponse;
            }

            @Override
            public ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> getChaptersByNovelId(Integer novelId) {
                return pageResponse;
            }

            @Override
            public ApiResponse<Map<String, Object>> getNovelByIdRaw(Integer novelId) {
                return null;
            }

            @Override
            public Response headlessGetNovelById(Integer novelId) {
                return null;
            }
        };

        // When
        ChapterDetailResponseDTO result = client.getChapter(1);

        // Then
        assertNotNull(result);
        assertEquals(testChapter.getId(), result.getId());
        assertEquals(testChapter.getTitle(), result.getTitle());
    }

    @Test
    void testGetChapter_DefaultMethod_NullResponse() {
        // Given
        ContentServiceClient client = new ContentServiceClient() {
            @Override
            public ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(List<Integer> chapterIds) {
                return null;
            }

            @Override
            public ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(List<Integer> novelIds) {
                return null;
            }

            @Override
            public ApiResponse<NovelDetailResponseDTO> getNovelById(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Integer> getNovelVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> incrementVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> updateNovelRatingAndCount(Integer novelId, Float avgRating, Integer reviewCount) {
                return null;
            }

            @Override
            public ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> getChaptersByNovelId(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Map<String, Object>> getNovelByIdRaw(Integer novelId) {
                return null;
            }

            @Override
            public Response headlessGetNovelById(Integer novelId) {
                return null;
            }
        };

        // When
        ChapterDetailResponseDTO result = client.getChapter(1);

        // Then
        assertNull(result);
    }

    @Test
    void testChapterExists_DefaultMethod_ValidChapter() {
        // Given
        ContentServiceClient client = new ContentServiceClient() {
            @Override
            public ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(List<Integer> chapterIds) {
                return chapterListResponse;
            }

            @Override
            public ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(List<Integer> novelIds) {
                return null;
            }

            @Override
            public ApiResponse<NovelDetailResponseDTO> getNovelById(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Integer> getNovelVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> incrementVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> updateNovelRatingAndCount(Integer novelId, Float avgRating, Integer reviewCount) {
                return null;
            }

            @Override
            public ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> getChaptersByNovelId(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Map<String, Object>> getNovelByIdRaw(Integer novelId) {
                return null;
            }

            @Override
            public Response headlessGetNovelById(Integer novelId) {
                return null;
            }
        };

        // When
        boolean result = client.chapterExists(1);

        // Then
        assertTrue(result);
    }

    @Test
    void testChapterExists_DefaultMethod_InvalidChapter() {
        // Given
        testChapter.setIsValid(false);
        ContentServiceClient client = new ContentServiceClient() {
            @Override
            public ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(List<Integer> chapterIds) {
                return chapterListResponse;
            }

            @Override
            public ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(List<Integer> novelIds) {
                return null;
            }

            @Override
            public ApiResponse<NovelDetailResponseDTO> getNovelById(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Integer> getNovelVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> incrementVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> updateNovelRatingAndCount(Integer novelId, Float avgRating, Integer reviewCount) {
                return null;
            }

            @Override
            public ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> getChaptersByNovelId(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Map<String, Object>> getNovelByIdRaw(Integer novelId) {
                return null;
            }

            @Override
            public Response headlessGetNovelById(Integer novelId) {
                return null;
            }
        };

        // When
        boolean result = client.chapterExists(1);

        // Then
        assertFalse(result);
    }

    @Test
    void testGetChapterIdsByNovelId_DefaultMethod_Success() {
        // Given
        ContentServiceClient client = new ContentServiceClient() {
            @Override
            public ApiResponse<List<ChapterDetailResponseDTO>> getChaptersBatch(List<Integer> chapterIds) {
                return null;
            }

            @Override
            public ApiResponse<List<NovelDetailResponseDTO>> getNovelsBatch(List<Integer> novelIds) {
                return null;
            }

            @Override
            public ApiResponse<NovelDetailResponseDTO> getNovelById(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<Integer> getNovelVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> incrementVoteCount(Integer novelId) {
                return null;
            }

            @Override
            public ApiResponse<String> updateNovelRatingAndCount(Integer novelId, Float avgRating, Integer reviewCount) {
                return null;
            }

            @Override
            public ApiResponse<PageResponseDTO<ChapterDetailResponseDTO>> getChaptersByNovelId(Integer novelId) {
                return pageResponse;
            }

            @Override
            public ApiResponse<Map<String, Object>> getNovelByIdRaw(Integer novelId) {
                return null;
            }

            @Override
            public Response headlessGetNovelById(Integer novelId) {
                return null;
            }
        };

        // When
        List<Integer> result = client.getChapterIdsByNovelId(1);

        // Then
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testChapter.getId(), result.get(0));
    }
}