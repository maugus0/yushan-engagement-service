package com.yushan.engagement_service.dto.novel;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NovelDetailResponseDTO
 */
class NovelDetailResponseDTOTest {

    @Test
    void testDefaultConstructor() {
        // Act
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUuid());
        assertNull(dto.getTitle());
        assertNull(dto.getAuthorId());
        assertNull(dto.getAuthorUsername());
        assertNull(dto.getCategoryId());
        assertNull(dto.getCategoryName());
        assertNull(dto.getSynopsis());
        assertNull(dto.getCoverImgUrl());
        assertNull(dto.getStatus());
        assertNull(dto.getIsCompleted());
        assertNull(dto.getChapterCnt());
        assertNull(dto.getWordCnt());
        assertNull(dto.getAvgRating());
        assertNull(dto.getReviewCnt());
        assertNull(dto.getViewCnt());
        assertNull(dto.getVoteCnt());
        assertNull(dto.getYuanCnt());
        assertNull(dto.getCreateTime());
        assertNull(dto.getUpdateTime());
        assertNull(dto.getPublishTime());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        Integer id = 1;
        UUID uuid = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        String title = "Test Novel";
        UUID authorId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        String authorUsername = "testauthor";
        Integer categoryId = 1;
        String categoryName = "Fantasy";
        String synopsis = "A test novel synopsis";
        String coverImgUrl = "https://example.com/cover.jpg";
        String status = "PUBLISHED";
        Boolean isCompleted = false;
        Integer chapterCnt = 10;
        Long wordCnt = 50000L;
        Float avgRating = 4.5f;
        Integer reviewCnt = 25;
        Long viewCnt = 1000L;
        Integer voteCnt = 100;
        Float yuanCnt = 10.5f;
        Date createTime = new Date(946684800000L);
        Date updateTime = new Date(946684800000L);
        Date publishTime = new Date(946684800000L);

        // Act
        dto.setId(id);
        dto.setUuid(uuid);
        dto.setTitle(title);
        dto.setAuthorId(authorId);
        dto.setAuthorUsername(authorUsername);
        dto.setCategoryId(categoryId);
        dto.setCategoryName(categoryName);
        dto.setSynopsis(synopsis);
        dto.setCoverImgUrl(coverImgUrl);
        dto.setStatus(status);
        dto.setIsCompleted(isCompleted);
        dto.setChapterCnt(chapterCnt);
        dto.setWordCnt(wordCnt);
        dto.setAvgRating(avgRating);
        dto.setReviewCnt(reviewCnt);
        dto.setViewCnt(viewCnt);
        dto.setVoteCnt(voteCnt);
        dto.setYuanCnt(yuanCnt);
        dto.setCreateTime(createTime);
        dto.setUpdateTime(updateTime);
        dto.setPublishTime(publishTime);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(uuid, dto.getUuid());
        assertEquals(title, dto.getTitle());
        assertEquals(authorId, dto.getAuthorId());
        assertEquals(authorUsername, dto.getAuthorUsername());
        assertEquals(categoryId, dto.getCategoryId());
        assertEquals(categoryName, dto.getCategoryName());
        assertEquals(synopsis, dto.getSynopsis());
        assertEquals(coverImgUrl, dto.getCoverImgUrl());
        assertEquals(status, dto.getStatus());
        assertEquals(isCompleted, dto.getIsCompleted());
        assertEquals(chapterCnt, dto.getChapterCnt());
        assertEquals(wordCnt, dto.getWordCnt());
        assertEquals(avgRating, dto.getAvgRating());
        assertEquals(reviewCnt, dto.getReviewCnt());
        assertEquals(viewCnt, dto.getViewCnt());
        assertEquals(voteCnt, dto.getVoteCnt());
        assertEquals(yuanCnt, dto.getYuanCnt());
        assertEquals(createTime, dto.getCreateTime());
        assertEquals(updateTime, dto.getUpdateTime());
        assertEquals(publishTime, dto.getPublishTime());
    }

    @Test
    void testCreateTimeGetterWithNullValue() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        dto.setCreateTime(null);

        // Act
        Date result = dto.getCreateTime();

        // Assert
        assertNull(result);
    }

    @Test
    void testCreateTimeSetterWithNullValue() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Act
        dto.setCreateTime(null);

        // Assert
        assertNull(dto.getCreateTime());
    }

    @Test
    void testCreateTimeGetterReturnsNewInstance() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setCreateTime(originalDate);

        // Act
        Date result1 = dto.getCreateTime();
        Date result2 = dto.getCreateTime();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testUpdateTimeGetterWithNullValue() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        dto.setUpdateTime(null);

        // Act
        Date result = dto.getUpdateTime();

        // Assert
        assertNull(result);
    }

    @Test
    void testUpdateTimeSetterWithNullValue() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Act
        dto.setUpdateTime(null);

        // Assert
        assertNull(dto.getUpdateTime());
    }

    @Test
    void testUpdateTimeGetterReturnsNewInstance() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setUpdateTime(originalDate);

        // Act
        Date result1 = dto.getUpdateTime();
        Date result2 = dto.getUpdateTime();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testPublishTimeGetterWithNullValue() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        dto.setPublishTime(null);

        // Act
        Date result = dto.getPublishTime();

        // Assert
        assertNull(result);
    }

    @Test
    void testPublishTimeSetterWithNullValue() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Act
        dto.setPublishTime(null);

        // Assert
        assertNull(dto.getPublishTime());
    }

    @Test
    void testPublishTimeGetterReturnsNewInstance() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setPublishTime(originalDate);

        // Act
        Date result1 = dto.getPublishTime();
        Date result2 = dto.getPublishTime();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testBooleanFields() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Act & Assert
        dto.setIsCompleted(true);
        assertTrue(dto.getIsCompleted());

        dto.setIsCompleted(false);
        assertFalse(dto.getIsCompleted());

        dto.setIsCompleted(null);
        assertNull(dto.getIsCompleted());
    }

    @Test
    void testNumericFields() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Act & Assert
        dto.setId(1);
        assertEquals(1, dto.getId());

        dto.setChapterCnt(10);
        assertEquals(10, dto.getChapterCnt());

        dto.setWordCnt(50000L);
        assertEquals(50000L, dto.getWordCnt());

        dto.setAvgRating(4.5f);
        assertEquals(4.5f, dto.getAvgRating());

        dto.setReviewCnt(25);
        assertEquals(25, dto.getReviewCnt());

        dto.setViewCnt(1000L);
        assertEquals(1000L, dto.getViewCnt());

        dto.setVoteCnt(100);
        assertEquals(100, dto.getVoteCnt());

        dto.setYuanCnt(10.5f);
        assertEquals(10.5f, dto.getYuanCnt());
    }

    @Test
    void testUUIDFields() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();
        UUID uuid1 = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        UUID uuid2 = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");

        // Act & Assert
        dto.setUuid(uuid1);
        assertEquals(uuid1, dto.getUuid());

        dto.setAuthorId(uuid2);
        assertEquals(uuid2, dto.getAuthorId());
    }

    @Test
    void testStringFields() {
        // Arrange
        NovelDetailResponseDTO dto = new NovelDetailResponseDTO();

        // Act & Assert
        dto.setTitle("Test Title");
        assertEquals("Test Title", dto.getTitle());

        dto.setAuthorUsername("testauthor");
        assertEquals("testauthor", dto.getAuthorUsername());

        dto.setCategoryName("Fantasy");
        assertEquals("Fantasy", dto.getCategoryName());

        dto.setSynopsis("Test synopsis");
        assertEquals("Test synopsis", dto.getSynopsis());

        dto.setCoverImgUrl("https://example.com/cover.jpg");
        assertEquals("https://example.com/cover.jpg", dto.getCoverImgUrl());

        dto.setStatus("PUBLISHED");
        assertEquals("PUBLISHED", dto.getStatus());
    }
}
