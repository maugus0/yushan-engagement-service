package com.yushan.engagement_service.entity;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.UUID;

class VoteTest {

    private Vote vote;
    private UUID testUserId;
    private Date testCreateTime;
    private Date testUpdateTime;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        testCreateTime = new Date();
        testUpdateTime = new Date(testCreateTime.getTime() + 1000);
        vote = new Vote();
    }

    @Test
    void testDefaultConstructor() {
        Vote newVote = new Vote();
        assertNotNull(newVote);
        assertNull(newVote.getId());
        assertNull(newVote.getUserId());
        assertNull(newVote.getNovelId());
        assertNull(newVote.getCreateTime());
        assertNull(newVote.getUpdateTime());
    }

    @Test
    void testParameterizedConstructor() {
        Vote newVote = new Vote(1, testUserId, 100, testCreateTime, testUpdateTime);
        
        assertEquals(1, newVote.getId());
        assertEquals(testUserId, newVote.getUserId());
        assertEquals(100, newVote.getNovelId());
        assertEquals(testCreateTime.getTime(), newVote.getCreateTime().getTime());
        assertEquals(testUpdateTime.getTime(), newVote.getUpdateTime().getTime());
    }

    @Test
    void testParameterizedConstructorWithNullDates() {
        Vote newVote = new Vote(1, testUserId, 100, null, null);
        
        assertEquals(1, newVote.getId());
        assertEquals(testUserId, newVote.getUserId());
        assertEquals(100, newVote.getNovelId());
        assertNull(newVote.getCreateTime());
        assertNull(newVote.getUpdateTime());
    }

    @Test
    void testIdGetterAndSetter() {
        vote.setId(123);
        assertEquals(123, vote.getId());
        
        vote.setId(null);
        assertNull(vote.getId());
    }

    @Test
    void testUserIdGetterAndSetter() {
        vote.setUserId(testUserId);
        assertEquals(testUserId, vote.getUserId());
        
        vote.setUserId(null);
        assertNull(vote.getUserId());
    }

    @Test
    void testNovelIdGetterAndSetter() {
        vote.setNovelId(456);
        assertEquals(456, vote.getNovelId());
        
        vote.setNovelId(0);
        assertEquals(0, vote.getNovelId());
        
        vote.setNovelId(null);
        assertNull(vote.getNovelId());
    }

    @Test
    void testCreateTimeGetterAndSetter() {
        vote.setCreateTime(testCreateTime);
        assertNotNull(vote.getCreateTime());
        assertEquals(testCreateTime.getTime(), vote.getCreateTime().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = vote.getCreateTime();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testCreateTime.getTime(), vote.getCreateTime().getTime());
        
        vote.setCreateTime(null);
        assertNull(vote.getCreateTime());
    }

    @Test
    void testUpdateTimeGetterAndSetter() {
        vote.setUpdateTime(testUpdateTime);
        assertNotNull(vote.getUpdateTime());
        assertEquals(testUpdateTime.getTime(), vote.getUpdateTime().getTime());
        
        // Test that it returns a copy, not the original
        Date originalTime = vote.getUpdateTime();
        originalTime.setTime(originalTime.getTime() + 1000);
        assertEquals(testUpdateTime.getTime(), vote.getUpdateTime().getTime());
        
        vote.setUpdateTime(null);
        assertNull(vote.getUpdateTime());
    }

    @Test
    void testDateImmutabilityInConstructor() {
        Date originalCreateTime = new Date();
        Date originalUpdateTime = new Date(originalCreateTime.getTime() + 1000);
        
        Vote newVote = new Vote(1, testUserId, 100, originalCreateTime, originalUpdateTime);
        
        // Modify original dates
        originalCreateTime.setTime(originalCreateTime.getTime() + 5000);
        originalUpdateTime.setTime(originalUpdateTime.getTime() + 5000);
        
        // Entity dates should not be affected
        assertNotEquals(originalCreateTime.getTime(), newVote.getCreateTime().getTime());
        assertNotEquals(originalUpdateTime.getTime(), newVote.getUpdateTime().getTime());
    }

    @Test
    void testDateImmutabilityInSetters() {
        Date originalTime = new Date();
        vote.setCreateTime(originalTime);
        
        // Modify original date
        originalTime.setTime(originalTime.getTime() + 5000);
        
        // Entity date should not be affected
        assertNotEquals(originalTime.getTime(), vote.getCreateTime().getTime());
    }

    @Test
    void testAllFieldsSet() {
        vote.setId(1);
        vote.setUserId(testUserId);
        vote.setNovelId(100);
        vote.setCreateTime(testCreateTime);
        vote.setUpdateTime(testUpdateTime);
        
        assertEquals(1, vote.getId());
        assertEquals(testUserId, vote.getUserId());
        assertEquals(100, vote.getNovelId());
        assertEquals(testCreateTime.getTime(), vote.getCreateTime().getTime());
        assertEquals(testUpdateTime.getTime(), vote.getUpdateTime().getTime());
    }

    @Test
    void testNovelIdBoundaryValues() {
        vote.setNovelId(1);
        assertEquals(1, vote.getNovelId());
        
        vote.setNovelId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, vote.getNovelId());
        
        vote.setNovelId(0);
        assertEquals(0, vote.getNovelId());
        
        vote.setNovelId(-1);
        assertEquals(-1, vote.getNovelId());
    }

    @Test
    void testIdBoundaryValues() {
        vote.setId(1);
        assertEquals(1, vote.getId());
        
        vote.setId(Integer.MAX_VALUE);
        assertEquals(Integer.MAX_VALUE, vote.getId());
        
        vote.setId(0);
        assertEquals(0, vote.getId());
        
        vote.setId(-1);
        assertEquals(-1, vote.getId());
    }

    @Test
    void testTimeConsistency() {
        Date createTime = new Date();
        Date updateTime = new Date(createTime.getTime() + 2000);
        
        vote.setCreateTime(createTime);
        vote.setUpdateTime(updateTime);
        
        assertTrue(vote.getUpdateTime().getTime() > vote.getCreateTime().getTime());
        assertEquals(2000, vote.getUpdateTime().getTime() - vote.getCreateTime().getTime());
    }

    @Test
    void testMultipleVotesSameUser() {
        UUID userId = UUID.randomUUID();
        
        Vote vote1 = new Vote();
        vote1.setUserId(userId);
        vote1.setNovelId(100);
        
        Vote vote2 = new Vote();
        vote2.setUserId(userId);
        vote2.setNovelId(200);
        
        assertEquals(vote1.getUserId(), vote2.getUserId());
        assertNotEquals(vote1.getNovelId(), vote2.getNovelId());
    }

    @Test
    void testMultipleVotesSameNovel() {
        Integer novelId = 100;
        
        Vote vote1 = new Vote();
        vote1.setUserId(UUID.randomUUID());
        vote1.setNovelId(novelId);
        
        Vote vote2 = new Vote();
        vote2.setUserId(UUID.randomUUID());
        vote2.setNovelId(novelId);
        
        assertEquals(vote1.getNovelId(), vote2.getNovelId());
        assertNotEquals(vote1.getUserId(), vote2.getUserId());
    }
}
