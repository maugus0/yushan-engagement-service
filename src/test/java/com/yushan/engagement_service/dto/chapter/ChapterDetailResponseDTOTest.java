package com.yushan.engagement_service.dto.chapter;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ChapterDetailResponseDTO
 */
class ChapterDetailResponseDTOTest {

    @Test
    void testDefaultConstructor() {
        // Act
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getUuid());
        assertNull(dto.getNovelId());
        assertNull(dto.getChapterNumber());
        assertNull(dto.getTitle());
        assertNull(dto.getContent());
        assertNull(dto.getPreview());
        assertNull(dto.getWordCnt());
        assertNull(dto.getIsPremium());
        assertNull(dto.getYuanCost());
        assertNull(dto.getViewCnt());
        assertNull(dto.getIsValid());
        assertNull(dto.getCreateTime());
        assertNull(dto.getUpdateTime());
        assertNull(dto.getPublishTime());
        assertNull(dto.getNextChapterUuid());
        assertNull(dto.getPreviousChapterUuid());
    }

    @Test
    void testParameterizedConstructor() {
        // Arrange
        Date now = new Date();
        UUID uuid = UUID.randomUUID();

        // Act
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO(
            1, uuid, 100, 1, "Test Chapter",
            "Chapter content", "Preview text", 1000, true, 5.0f,
            100L, true, now, now, now
        );

        // Assert
        assertEquals(1, dto.getId());
        assertEquals(uuid, dto.getUuid());
        assertEquals(100, dto.getNovelId());
        assertEquals(1, dto.getChapterNumber());
        assertEquals("Test Chapter", dto.getTitle());
        assertEquals("Chapter content", dto.getContent());
        assertEquals("Preview text", dto.getPreview());
        assertEquals(1000, dto.getWordCnt());
        assertTrue(dto.getIsPremium());
        assertEquals(5.0f, dto.getYuanCost());
        assertEquals(100L, dto.getViewCnt());
        assertTrue(dto.getIsValid());
        assertNotNull(dto.getCreateTime());
        assertNotNull(dto.getUpdateTime());
        assertNotNull(dto.getPublishTime());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();
        Date now = new Date();
        UUID uuid = UUID.randomUUID();
        UUID nextUuid = UUID.randomUUID();
        UUID prevUuid = UUID.randomUUID();

        // Act & Assert
        dto.setId(1);
        assertEquals(1, dto.getId());

        dto.setUuid(uuid);
        assertEquals(uuid, dto.getUuid());

        dto.setNovelId(100);
        assertEquals(100, dto.getNovelId());

        dto.setChapterNumber(1);
        assertEquals(1, dto.getChapterNumber());

        dto.setTitle("Test Chapter");
        assertEquals("Test Chapter", dto.getTitle());

        dto.setContent("Chapter content");
        assertEquals("Chapter content", dto.getContent());

        dto.setPreview("Preview text");
        assertEquals("Preview text", dto.getPreview());

        dto.setWordCnt(1000);
        assertEquals(1000, dto.getWordCnt());

        dto.setIsPremium(true);
        assertTrue(dto.getIsPremium());

        dto.setYuanCost(5.0f);
        assertEquals(5.0f, dto.getYuanCost());

        dto.setViewCnt(100L);
        assertEquals(100L, dto.getViewCnt());

        dto.setIsValid(true);
        assertTrue(dto.getIsValid());

        dto.setCreateTime(now);
        assertEquals(now, dto.getCreateTime());

        dto.setUpdateTime(now);
        assertEquals(now, dto.getUpdateTime());

        dto.setPublishTime(now);
        assertEquals(now, dto.getPublishTime());

        dto.setNextChapterUuid(nextUuid);
        assertEquals(nextUuid, dto.getNextChapterUuid());

        dto.setPreviousChapterUuid(prevUuid);
        assertEquals(prevUuid, dto.getPreviousChapterUuid());
    }

    @Test
    void testDateCloning() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();
        Date originalDate = new Date();
        Date modifiedDate = new Date(originalDate.getTime() + 1000);

        // Act
        dto.setCreateTime(originalDate);
        dto.setUpdateTime(originalDate);
        dto.setPublishTime(originalDate);

        // Modify original date
        originalDate.setTime(modifiedDate.getTime());

        // Assert - DTO should have cloned dates, not references
        assertNotEquals(originalDate, dto.getCreateTime());
        assertNotEquals(originalDate, dto.getUpdateTime());
        assertNotEquals(originalDate, dto.getPublishTime());
    }

    @Test
    void testNullDateHandling() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();

        // Act
        dto.setCreateTime(null);
        dto.setUpdateTime(null);
        dto.setPublishTime(null);

        // Assert
        assertNull(dto.getCreateTime());
        assertNull(dto.getUpdateTime());
        assertNull(dto.getPublishTime());
    }

    @Test
    void testBooleanValues() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();

        // Act & Assert - Test false values
        dto.setIsPremium(false);
        assertFalse(dto.getIsPremium());

        dto.setIsValid(false);
        assertFalse(dto.getIsValid());

        // Act & Assert - Test true values
        dto.setIsPremium(true);
        assertTrue(dto.getIsPremium());

        dto.setIsValid(true);
        assertTrue(dto.getIsValid());
    }

    @Test
    void testNumericValues() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();

        // Act & Assert
        dto.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, dto.getId());

        dto.setNovelId(Integer.MIN_VALUE);
        assertEquals(Integer.MIN_VALUE, dto.getNovelId());

        dto.setChapterNumber(0);
        assertEquals(0, dto.getChapterNumber());

        dto.setWordCnt(-1);
        assertEquals(-1, dto.getWordCnt());

        dto.setYuanCost(0.0f);
        assertEquals(0.0f, dto.getYuanCost());

        dto.setViewCnt(Long.MAX_VALUE);
        assertEquals(Long.MAX_VALUE, dto.getViewCnt());
    }

    @Test
    void testStringValues() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();

        // Act & Assert
        dto.setTitle("");
        assertEquals("", dto.getTitle());

        dto.setContent("Very long content that exceeds normal length and contains special characters: !@#$%^&*()");
        assertEquals("Very long content that exceeds normal length and contains special characters: !@#$%^&*()", dto.getContent());

        dto.setPreview(null);
        assertNull(dto.getPreview());
    }

    @Test
    void testUuidValues() {
        // Arrange
        ChapterDetailResponseDTO dto = new ChapterDetailResponseDTO();
        UUID uuid1 = UUID.randomUUID();
        UUID uuid2 = UUID.randomUUID();
        UUID uuid3 = UUID.randomUUID();

        // Act & Assert
        dto.setUuid(uuid1);
        assertEquals(uuid1, dto.getUuid());

        dto.setNextChapterUuid(uuid2);
        assertEquals(uuid2, dto.getNextChapterUuid());

        dto.setPreviousChapterUuid(uuid3);
        assertEquals(uuid3, dto.getPreviousChapterUuid());

        // Test null UUIDs
        dto.setUuid(null);
        assertNull(dto.getUuid());

        dto.setNextChapterUuid(null);
        assertNull(dto.getNextChapterUuid());

        dto.setPreviousChapterUuid(null);
        assertNull(dto.getPreviousChapterUuid());
    }
}
