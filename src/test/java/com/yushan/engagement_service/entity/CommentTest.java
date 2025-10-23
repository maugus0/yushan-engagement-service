package com.yushan.engagement_service.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

class CommentTest {

    private Comment comment;
    private UUID testUserId;
    private Date testCreateTime;
    private Date testUpdateTime;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testCreateTime = new Date();
        testUpdateTime = new Date(testCreateTime.getTime() + 1000);
        comment = new Comment();
    }

    @Test
    void testDefaultConstructor() {
        Comment newComment = new Comment();
        assertNotNull(newComment);
        assertNull(newComment.getId());
        assertNull(newComment.getUserId());
        assertNull(newComment.getChapterId());
        assertNull(newComment.getContent());
        assertNull(newComment.getLikeCnt());
        assertNull(newComment.getIsSpoiler());
        assertNull(newComment.getCreateTime());
        assertNull(newComment.getUpdateTime());
    }

    @Test
    void testParameterizedConstructor() {
        Comment newComment = new Comment(1, testUserId, 100, "Test content", 5, true, testCreateTime, testUpdateTime);
        
        assertEquals(1, newComment.getId());
        assertEquals(testUserId, newComment.getUserId());
        assertEquals(100, newComment.getChapterId());
        assertEquals("Test content", newComment.getContent());
        assertEquals(5, newComment.getLikeCnt());
        assertTrue(newComment.getIsSpoiler());
        assertEquals(testCreateTime.getTime(), newComment.getCreateTime().getTime());
        assertEquals(testUpdateTime.getTime(), newComment.getUpdateTime().getTime());
    }

    @Test
    void testParameterizedConstructorWithNullDates() {
        Comment newComment = new Comment(1, testUserId, 100, "Test content", 5, true, null, null);
        
        assertEquals(1, newComment.getId());
        assertEquals(testUserId, newComment.getUserId());
        assertEquals(100, newComment.getChapterId());
        assertEquals("Test content", newComment.getContent());
        assertEquals(5, newComment.getLikeCnt());
        assertTrue(newComment.getIsSpoiler());
        assertNull(newComment.getCreateTime());
        assertNull(newComment.getUpdateTime());
    }

    @Test
    void testIdGetterAndSetter() {
        comment.setId(123);
        assertEquals(123, comment.getId());
        
        comment.setId(null);
        assertNull(comment.getId());
    }

    @Test
    void testUserIdGetterAndSetter() {
        comment.setUserId(testUserId);
        assertEquals(testUserId, comment.getUserId());
        
        comment.setUserId(null);
        assertNull(comment.getUserId());
    }

    @Test
    void testChapterIdGetterAndSetter() {
        comment.setChapterId(456);
        assertEquals(456, comment.getChapterId());
        
        comment.setChapterId(null);
        assertNull(comment.getChapterId());
    }

    @Test
    void testContentGetterAndSetter() {
        comment.setContent("Test content");
        assertEquals("Test content", comment.getContent());
        
        comment.setContent("  Trimmed content  ");
        assertEquals("Trimmed content", comment.getContent());
        
        comment.setContent(null);
        assertNull(comment.getContent());
    }

    @Test
    void testLikeCntGetterAndSetter() {
        comment.setLikeCnt(10);
        assertEquals(10, comment.getLikeCnt());
        
        comment.setLikeCnt(0);
        assertEquals(0, comment.getLikeCnt());
        
        comment.setLikeCnt(null);
        assertNull(comment.getLikeCnt());
    }

    @Test
    void testIsSpoilerGetterAndSetter() {
        comment.setIsSpoiler(true);
        assertTrue(comment.getIsSpoiler());
        
        comment.setIsSpoiler(false);
        assertFalse(comment.getIsSpoiler());
        
        comment.setIsSpoiler(null);
        assertNull(comment.getIsSpoiler());
    }

    @Test
    void testCreateTimeGetterAndSetter() {
        comment.setCreateTime(testCreateTime);
        assertNotNull(comment.getCreateTime());
        assertEquals(testCreateTime.getTime(), comment.getCreateTime().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = comment.getCreateTime();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testCreateTime.getTime(), comment.getCreateTime().getTime());
        
        comment.setCreateTime(null);
        assertNull(comment.getCreateTime());
    }

    @Test
    void testUpdateTimeGetterAndSetter() {
        comment.setUpdateTime(testUpdateTime);
        assertNotNull(comment.getUpdateTime());
        assertEquals(testUpdateTime.getTime(), comment.getUpdateTime().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = comment.getUpdateTime();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testUpdateTime.getTime(), comment.getUpdateTime().getTime());
        
        comment.setUpdateTime(null);
        assertNull(comment.getUpdateTime());
    }

    @Test
    void testDateImmutabilityInConstructor() {
        Date originalCreateTime = new Date();
        Date originalUpdateTime = new Date(originalCreateTime.getTime() + 1000);
        
        Comment newComment = new Comment(1, testUserId, 100, "Test", 0, false, originalCreateTime, originalUpdateTime);
        
        // Modify original dates
        originalCreateTime.setTime(originalCreateTime.getTime() + 5000);
        originalUpdateTime.setTime(originalUpdateTime.getTime() + 5000);
        
        // Entity dates should not be affected
        assertNotEquals(originalCreateTime.getTime(), newComment.getCreateTime().getTime());
        assertNotEquals(originalUpdateTime.getTime(), newComment.getUpdateTime().getTime());
    }

    @Test
    void testDateImmutabilityInSetters() {
        Date originalTime = new Date();
        comment.setCreateTime(originalTime);
        
        // Modify original date
        originalTime.setTime(originalTime.getTime() + 5000);
        
        // Entity date should not be affected
        assertNotEquals(originalTime.getTime(), comment.getCreateTime().getTime());
    }

    @Test
    void testAllFieldsSet() {
        comment.setId(1);
        comment.setUserId(testUserId);
        comment.setChapterId(100);
        comment.setContent("Complete test content");
        comment.setLikeCnt(15);
        comment.setIsSpoiler(false);
        comment.setCreateTime(testCreateTime);
        comment.setUpdateTime(testUpdateTime);
        
        assertEquals(1, comment.getId());
        assertEquals(testUserId, comment.getUserId());
        assertEquals(100, comment.getChapterId());
        assertEquals("Complete test content", comment.getContent());
        assertEquals(15, comment.getLikeCnt());
        assertFalse(comment.getIsSpoiler());
        assertEquals(testCreateTime.getTime(), comment.getCreateTime().getTime());
        assertEquals(testUpdateTime.getTime(), comment.getUpdateTime().getTime());
    }
}
