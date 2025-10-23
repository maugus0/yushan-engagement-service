package com.yushan.engagement_service.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

class ReviewTest {

    private Review review;
    private UUID testUuid;
    private UUID testUserId;
    private Date testCreateTime;
    private Date testUpdateTime;

    @BeforeEach
    void setUp() {
        testUuid = UUID.randomUUID();
        testUserId = UUID.randomUUID();
        testCreateTime = new Date();
        testUpdateTime = new Date(testCreateTime.getTime() + 1000);
        review = new Review();
    }

    @Test
    void testDefaultConstructor() {
        Review newReview = new Review();
        assertNotNull(newReview);
        assertNull(newReview.getId());
        assertNull(newReview.getUuid());
        assertNull(newReview.getUserId());
        assertNull(newReview.getNovelId());
        assertNull(newReview.getRating());
        assertNull(newReview.getTitle());
        assertNull(newReview.getContent());
        assertNull(newReview.getLikeCnt());
        assertNull(newReview.getIsSpoiler());
        assertNull(newReview.getCreateTime());
        assertNull(newReview.getUpdateTime());
    }

    @Test
    void testParameterizedConstructor() {
        Review newReview = new Review(1, testUuid, testUserId, 100, 5, "Great novel", 
                "Excellent story", 10, false, testCreateTime, testUpdateTime);
        
        assertEquals(1, newReview.getId());
        assertEquals(testUuid, newReview.getUuid());
        assertEquals(testUserId, newReview.getUserId());
        assertEquals(100, newReview.getNovelId());
        assertEquals(5, newReview.getRating());
        assertEquals("Great novel", newReview.getTitle());
        assertEquals("Excellent story", newReview.getContent());
        assertEquals(10, newReview.getLikeCnt());
        assertFalse(newReview.getIsSpoiler());
        assertEquals(testCreateTime.getTime(), newReview.getCreateTime().getTime());
        assertEquals(testUpdateTime.getTime(), newReview.getUpdateTime().getTime());
    }

    @Test
    void testParameterizedConstructorWithNullDates() {
        Review newReview = new Review(1, testUuid, testUserId, 100, 5, "Great novel", 
                "Excellent story", 10, false, null, null);
        
        assertEquals(1, newReview.getId());
        assertEquals(testUuid, newReview.getUuid());
        assertEquals(testUserId, newReview.getUserId());
        assertEquals(100, newReview.getNovelId());
        assertEquals(5, newReview.getRating());
        assertEquals("Great novel", newReview.getTitle());
        assertEquals("Excellent story", newReview.getContent());
        assertEquals(10, newReview.getLikeCnt());
        assertFalse(newReview.getIsSpoiler());
        assertNull(newReview.getCreateTime());
        assertNull(newReview.getUpdateTime());
    }

    @Test
    void testIdGetterAndSetter() {
        review.setId(123);
        assertEquals(123, review.getId());
        
        review.setId(null);
        assertNull(review.getId());
    }

    @Test
    void testUuidGetterAndSetter() {
        review.setUuid(testUuid);
        assertEquals(testUuid, review.getUuid());
        
        review.setUuid(null);
        assertNull(review.getUuid());
    }

    @Test
    void testUserIdGetterAndSetter() {
        review.setUserId(testUserId);
        assertEquals(testUserId, review.getUserId());
        
        review.setUserId(null);
        assertNull(review.getUserId());
    }

    @Test
    void testNovelIdGetterAndSetter() {
        review.setNovelId(456);
        assertEquals(456, review.getNovelId());
        
        review.setNovelId(0);
        assertEquals(0, review.getNovelId());
        
        review.setNovelId(null);
        assertNull(review.getNovelId());
    }

    @Test
    void testRatingGetterAndSetter() {
        review.setRating(5);
        assertEquals(5, review.getRating());
        
        review.setRating(1);
        assertEquals(1, review.getRating());
        
        review.setRating(null);
        assertNull(review.getRating());
    }

    @Test
    void testTitleGetterAndSetter() {
        review.setTitle("Great novel");
        assertEquals("Great novel", review.getTitle());
        
        review.setTitle("  Amazing story  ");
        assertEquals("Amazing story", review.getTitle());
        
        review.setTitle(null);
        assertNull(review.getTitle());
    }

    @Test
    void testContentGetterAndSetter() {
        review.setContent("Excellent story");
        assertEquals("Excellent story", review.getContent());
        
        review.setContent("  Great content  ");
        assertEquals("Great content", review.getContent());
        
        review.setContent(null);
        assertNull(review.getContent());
    }

    @Test
    void testLikeCntGetterAndSetter() {
        review.setLikeCnt(15);
        assertEquals(15, review.getLikeCnt());
        
        review.setLikeCnt(0);
        assertEquals(0, review.getLikeCnt());
        
        review.setLikeCnt(null);
        assertNull(review.getLikeCnt());
    }

    @Test
    void testIsSpoilerGetterAndSetter() {
        review.setIsSpoiler(true);
        assertTrue(review.getIsSpoiler());
        
        review.setIsSpoiler(false);
        assertFalse(review.getIsSpoiler());
        
        review.setIsSpoiler(null);
        assertNull(review.getIsSpoiler());
    }

    @Test
    void testCreateTimeGetterAndSetter() {
        review.setCreateTime(testCreateTime);
        assertNotNull(review.getCreateTime());
        assertEquals(testCreateTime.getTime(), review.getCreateTime().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = review.getCreateTime();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testCreateTime.getTime(), review.getCreateTime().getTime());
        
        review.setCreateTime(null);
        assertNull(review.getCreateTime());
    }

    @Test
    void testUpdateTimeGetterAndSetter() {
        review.setUpdateTime(testUpdateTime);
        assertNotNull(review.getUpdateTime());
        assertEquals(testUpdateTime.getTime(), review.getUpdateTime().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = review.getUpdateTime();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testUpdateTime.getTime(), review.getUpdateTime().getTime());
        
        review.setUpdateTime(null);
        assertNull(review.getUpdateTime());
    }

    @Test
    void testDateImmutabilityInConstructor() {
        Date originalCreateTime = new Date();
        Date originalUpdateTime = new Date(originalCreateTime.getTime() + 1000);
        
        Review newReview = new Review(1, testUuid, testUserId, 100, 5, "Title", 
                "Content", 0, false, originalCreateTime, originalUpdateTime);
        
        // Modify original dates
        originalCreateTime.setTime(originalCreateTime.getTime() + 5000);
        originalUpdateTime.setTime(originalUpdateTime.getTime() + 5000);
        
        // Entity dates should not be affected
        assertNotEquals(originalCreateTime.getTime(), newReview.getCreateTime().getTime());
        assertNotEquals(originalUpdateTime.getTime(), newReview.getUpdateTime().getTime());
    }

    @Test
    void testDateImmutabilityInSetters() {
        Date originalTime = new Date();
        review.setCreateTime(originalTime);
        
        // Modify original date
        originalTime.setTime(originalTime.getTime() + 5000);
        
        // Entity date should not be affected
        assertNotEquals(originalTime.getTime(), review.getCreateTime().getTime());
    }

    @Test
    void testAllFieldsSet() {
        review.setId(1);
        review.setUuid(testUuid);
        review.setUserId(testUserId);
        review.setNovelId(100);
        review.setRating(5);
        review.setTitle("Complete test title");
        review.setContent("Complete test content");
        review.setLikeCnt(20);
        review.setIsSpoiler(true);
        review.setCreateTime(testCreateTime);
        review.setUpdateTime(testUpdateTime);
        
        assertEquals(1, review.getId());
        assertEquals(testUuid, review.getUuid());
        assertEquals(testUserId, review.getUserId());
        assertEquals(100, review.getNovelId());
        assertEquals(5, review.getRating());
        assertEquals("Complete test title", review.getTitle());
        assertEquals("Complete test content", review.getContent());
        assertEquals(20, review.getLikeCnt());
        assertTrue(review.getIsSpoiler());
        assertEquals(testCreateTime.getTime(), review.getCreateTime().getTime());
        assertEquals(testUpdateTime.getTime(), review.getUpdateTime().getTime());
    }

    @Test
    void testStringTrimmingBehavior() {
        review.setTitle("  Great Novel  ");
        assertEquals("Great Novel", review.getTitle());
        
        review.setContent("  Excellent Story  ");
        assertEquals("Excellent Story", review.getContent());
    }

    @Test
    void testRatingBoundaryValues() {
        review.setRating(1);
        assertEquals(1, review.getRating());
        
        review.setRating(5);
        assertEquals(5, review.getRating());
        
        review.setRating(0);
        assertEquals(0, review.getRating());
        
        review.setRating(-1);
        assertEquals(-1, review.getRating());
    }

    @Test
    void testNovelIdBoundaryValues() {
        review.setNovelId(1);
        assertEquals(1, review.getNovelId());
        
        review.setNovelId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, review.getNovelId());
        
        review.setNovelId(0);
        assertEquals(0, review.getNovelId());
        
        review.setNovelId(-1);
        assertEquals(-1, review.getNovelId());
    }
}
