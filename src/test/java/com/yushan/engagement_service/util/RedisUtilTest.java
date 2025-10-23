package com.yushan.engagement_service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for RedisUtil
 */
@ExtendWith(MockitoExtension.class)
class RedisUtilTest {

    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    @Mock
    private ValueOperations<String, Object> valueOperations;

    @InjectMocks
    private RedisUtil redisUtil;

    @BeforeEach
    void setUp() {
        // Only mock when needed in individual tests
    }

    @Test
    void set_WithDuration_ShouldCallRedisTemplate() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        String value = "test:value";
        Duration ttl = Duration.ofHours(1);

        // Act
        redisUtil.set(key, value, ttl);

        // Assert
        verify(valueOperations).set(key, value, ttl);
    }

    @Test
    void set_WithSeconds_ShouldCallRedisTemplate() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        String value = "test:value";
        long ttlSeconds = 3600;

        // Act
        redisUtil.set(key, value, ttlSeconds);

        // Assert
        verify(valueOperations).set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    @Test
    void get_ShouldReturnValue() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        String expectedValue = "test:value";
        when(valueOperations.get(key)).thenReturn(expectedValue);

        // Act
        Object result = redisUtil.get(key);

        // Assert
        assertEquals(expectedValue, result);
        verify(valueOperations).get(key);
    }

    @Test
    void get_WithClass_ShouldReturnTypedValue() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        String expectedValue = "test:value";
        when(valueOperations.get(key)).thenReturn(expectedValue);

        // Act
        String result = redisUtil.get(key, String.class);

        // Assert
        assertEquals(expectedValue, result);
        verify(valueOperations).get(key);
    }

    @Test
    void get_WithClass_WhenValueIsNull_ShouldReturnNull() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        when(valueOperations.get(key)).thenReturn(null);

        // Act
        String result = redisUtil.get(key, String.class);

        // Assert
        assertNull(result);
        verify(valueOperations).get(key);
    }

    @Test
    void get_WithClass_WhenValueIsWrongType_ShouldReturnNull() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        Integer wrongTypeValue = 123;
        when(valueOperations.get(key)).thenReturn(wrongTypeValue);

        // Act
        String result = redisUtil.get(key, String.class);

        // Assert
        assertNull(result);
        verify(valueOperations).get(key);
    }

    @Test
    void delete_WithSingleKey_ShouldCallRedisTemplate() {
        // Arrange
        String key = "test:key";

        // Act
        redisUtil.delete(key);

        // Assert
        verify(redisTemplate).delete(key);
    }

    @Test
    void delete_WithMultipleKeys_ShouldCallRedisTemplate() {
        // Arrange
        Set<String> keys = new HashSet<>();
        keys.add("key1");
        keys.add("key2");

        // Act
        redisUtil.delete(keys);

        // Assert
        verify(redisTemplate).delete(keys);
    }

    @Test
    void exists_WhenKeyExists_ShouldReturnTrue() {
        // Arrange
        String key = "test:key";
        when(redisTemplate.hasKey(key)).thenReturn(true);

        // Act
        boolean result = redisUtil.exists(key);

        // Assert
        assertTrue(result);
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void exists_WhenKeyDoesNotExist_ShouldReturnFalse() {
        // Arrange
        String key = "test:key";
        when(redisTemplate.hasKey(key)).thenReturn(false);

        // Act
        boolean result = redisUtil.exists(key);

        // Assert
        assertFalse(result);
        verify(redisTemplate).hasKey(key);
    }

    @Test
    void increment_ShouldReturnIncrementedValue() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        Long expectedValue = 1L;
        when(valueOperations.increment(key)).thenReturn(expectedValue);

        // Act
        Long result = redisUtil.increment(key);

        // Assert
        assertEquals(expectedValue, result);
        verify(valueOperations).increment(key);
    }

    @Test
    void increment_WithDelta_ShouldReturnIncrementedValue() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String key = "test:key";
        long delta = 5;
        Long expectedValue = 5L;
        when(valueOperations.increment(key, delta)).thenReturn(expectedValue);

        // Act
        Long result = redisUtil.increment(key, delta);

        // Assert
        assertEquals(expectedValue, result);
        verify(valueOperations).increment(key, delta);
    }

    @Test
    void expire_ShouldCallRedisTemplate() {
        // Arrange
        String key = "test:key";
        Duration ttl = Duration.ofHours(1);

        // Act
        redisUtil.expire(key, ttl);

        // Assert
        verify(redisTemplate).expire(key, ttl);
    }

    @Test
    void keys_ShouldReturnMatchingKeys() {
        // Arrange
        String pattern = "test:*";
        Set<String> expectedKeys = new HashSet<>();
        expectedKeys.add("test:key1");
        expectedKeys.add("test:key2");
        when(redisTemplate.keys(pattern)).thenReturn(expectedKeys);

        // Act
        Set<String> result = redisUtil.keys(pattern);

        // Assert
        assertEquals(expectedKeys, result);
        verify(redisTemplate).keys(pattern);
    }

    // Comment-specific cache methods tests

    @Test
    void cacheComment_ShouldSetCommentWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer commentId = 1;
        String commentData = "comment data";

        // Act
        redisUtil.cacheComment(commentId, commentData);

        // Assert
        verify(valueOperations).set("comment:1", commentData, Duration.ofHours(1));
    }

    @Test
    void getCachedComment_ShouldReturnCommentData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer commentId = 1;
        String expectedData = "comment data";
        when(valueOperations.get("comment:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedComment(commentId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("comment:1");
    }

    @Test
    void getCachedComment_WithClass_ShouldReturnTypedCommentData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer commentId = 1;
        String expectedData = "comment data";
        when(valueOperations.get("comment:1")).thenReturn(expectedData);

        // Act
        String result = redisUtil.getCachedComment(commentId, String.class);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("comment:1");
    }

    @Test
    void cacheChapterComments_ShouldSetChapterCommentsWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer chapterId = 1;
        String commentsData = "comments data";

        // Act
        redisUtil.cacheChapterComments(chapterId, commentsData);

        // Assert
        verify(valueOperations).set("comment:chapter:1", commentsData, Duration.ofHours(1));
    }

    @Test
    void getCachedChapterComments_ShouldReturnChapterCommentsData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer chapterId = 1;
        String expectedData = "comments data";
        when(valueOperations.get("comment:chapter:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedChapterComments(chapterId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("comment:chapter:1");
    }

    @Test
    void deleteCommentCache_ShouldDeleteCommentKey() {
        // Arrange
        Integer commentId = 1;

        // Act
        redisUtil.deleteCommentCache(commentId);

        // Assert
        verify(redisTemplate).delete("comment:1");
    }

    // Review-specific cache methods tests

    @Test
    void cacheReview_ShouldSetReviewWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer reviewId = 1;
        String reviewData = "review data";

        // Act
        redisUtil.cacheReview(reviewId, reviewData);

        // Assert
        verify(valueOperations).set("review:1", reviewData, Duration.ofHours(2));
    }

    @Test
    void getCachedReview_ShouldReturnReviewData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer reviewId = 1;
        String expectedData = "review data";
        when(valueOperations.get("review:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedReview(reviewId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("review:1");
    }

    @Test
    void getCachedReview_WithClass_ShouldReturnTypedReviewData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer reviewId = 1;
        String expectedData = "review data";
        when(valueOperations.get("review:1")).thenReturn(expectedData);

        // Act
        String result = redisUtil.getCachedReview(reviewId, String.class);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("review:1");
    }

    @Test
    void cacheNovelReviews_ShouldSetNovelReviewsWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer novelId = 1;
        String reviewsData = "reviews data";

        // Act
        redisUtil.cacheNovelReviews(novelId, reviewsData);

        // Assert
        verify(valueOperations).set("review:novel:1", reviewsData, Duration.ofHours(2));
    }

    @Test
    void getCachedNovelReviews_ShouldReturnNovelReviewsData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer novelId = 1;
        String expectedData = "reviews data";
        when(valueOperations.get("review:novel:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedNovelReviews(novelId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("review:novel:1");
    }

    @Test
    void deleteReviewCache_ShouldDeleteReviewKey() {
        // Arrange
        Integer reviewId = 1;

        // Act
        redisUtil.deleteReviewCache(reviewId);

        // Assert
        verify(redisTemplate).delete("review:1");
    }

    // Vote-specific cache methods tests

    @Test
    void cacheVote_ShouldSetVoteWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer voteId = 1;
        String voteData = "vote data";

        // Act
        redisUtil.cacheVote(voteId, voteData);

        // Assert
        verify(valueOperations).set("vote:1", voteData, Duration.ofHours(1));
    }

    @Test
    void getCachedVote_ShouldReturnVoteData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer voteId = 1;
        String expectedData = "vote data";
        when(valueOperations.get("vote:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedVote(voteId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("vote:1");
    }

    @Test
    void cacheNovelVotes_ShouldSetNovelVotesWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer novelId = 1;
        String votesData = "votes data";

        // Act
        redisUtil.cacheNovelVotes(novelId, votesData);

        // Assert
        verify(valueOperations).set("vote:novel:1", votesData, Duration.ofHours(1));
    }

    @Test
    void getCachedNovelVotes_ShouldReturnNovelVotesData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        Integer novelId = 1;
        String expectedData = "votes data";
        when(valueOperations.get("vote:novel:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedNovelVotes(novelId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("vote:novel:1");
    }

    @Test
    void cacheUserVotes_ShouldSetUserVotesWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String userId = "user123";
        String votesData = "votes data";

        // Act
        redisUtil.cacheUserVotes(userId, votesData);

        // Assert
        verify(valueOperations).set("vote:user:user123", votesData, Duration.ofHours(1));
    }

    @Test
    void getCachedUserVotes_ShouldReturnUserVotesData() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String userId = "user123";
        String expectedData = "votes data";
        when(valueOperations.get("vote:user:user123")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedUserVotes(userId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("vote:user:user123");
    }

    // Like-specific cache methods tests

    @Test
    void cacheLikeCount_ShouldSetLikeCountWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String entityType = "comment";
        Integer entityId = 1;
        Long likeCount = 5L;

        // Act
        redisUtil.cacheLikeCount(entityType, entityId, likeCount);

        // Assert
        verify(valueOperations).set("like:comment:1", likeCount, Duration.ofMinutes(30));
    }

    @Test
    void getCachedLikeCount_ShouldReturnLikeCount() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String entityType = "comment";
        Integer entityId = 1;
        Long expectedCount = 5L;
        when(valueOperations.get("like:comment:1")).thenReturn(expectedCount);

        // Act
        Long result = redisUtil.getCachedLikeCount(entityType, entityId);

        // Assert
        assertEquals(expectedCount, result);
        verify(valueOperations).get("like:comment:1");
    }

    @Test
    void incrementCachedLikeCount_ShouldIncrementAndSetExpiration() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String entityType = "comment";
        Integer entityId = 1;
        Long newCount = 6L;
        when(valueOperations.increment("like:comment:1")).thenReturn(newCount);

        // Act
        Long result = redisUtil.incrementCachedLikeCount(entityType, entityId);

        // Assert
        assertEquals(newCount, result);
        verify(valueOperations).increment("like:comment:1");
        verify(redisTemplate).expire("like:comment:1", Duration.ofMinutes(30));
    }

    @Test
    void decrementCachedLikeCount_ShouldDecrementAndSetExpiration() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String entityType = "comment";
        Integer entityId = 1;
        Long newCount = 4L;
        when(valueOperations.increment("like:comment:1", -1)).thenReturn(newCount);

        // Act
        Long result = redisUtil.decrementCachedLikeCount(entityType, entityId);

        // Assert
        assertEquals(newCount, result);
        verify(valueOperations).increment("like:comment:1", -1);
        verify(redisTemplate).expire("like:comment:1", Duration.ofMinutes(30));
    }

    // Engagement statistics cache methods tests

    @Test
    void cacheEngagementStats_ShouldSetEngagementStatsWithCorrectKey() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String entityType = "novel";
        Integer entityId = 1;
        String statsData = "stats data";

        // Act
        redisUtil.cacheEngagementStats(entityType, entityId, statsData);

        // Assert
        verify(valueOperations).set("engagement:novel:1", statsData, Duration.ofMinutes(15));
    }

    @Test
    void getCachedEngagementStats_ShouldReturnEngagementStats() {
        // Arrange
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
        String entityType = "novel";
        Integer entityId = 1;
        String expectedData = "stats data";
        when(valueOperations.get("engagement:novel:1")).thenReturn(expectedData);

        // Act
        Object result = redisUtil.getCachedEngagementStats(entityType, entityId);

        // Assert
        assertEquals(expectedData, result);
        verify(valueOperations).get("engagement:novel:1");
    }

    // Cache invalidation methods tests

    @Test
    void invalidateCommentCaches_ShouldDeleteCommentAndChapterCaches() {
        // Arrange
        Integer commentId = 1;
        Set<String> commentKeys = new HashSet<>();
        commentKeys.add("comment:chapter:1");
        commentKeys.add("comment:chapter:2");
        when(redisTemplate.keys("comment:chapter:*")).thenReturn(commentKeys);

        // Act
        redisUtil.invalidateCommentCaches(commentId);

        // Assert
        verify(redisTemplate).delete("comment:1");
        verify(redisTemplate).keys("comment:chapter:*");
        verify(redisTemplate).delete(commentKeys);
    }

    @Test
    void invalidateCommentCaches_WhenNoChapterKeys_ShouldOnlyDeleteComment() {
        // Arrange
        Integer commentId = 1;
        when(redisTemplate.keys("comment:chapter:*")).thenReturn(new HashSet<>());

        // Act
        redisUtil.invalidateCommentCaches(commentId);

        // Assert
        verify(redisTemplate).delete("comment:1");
        verify(redisTemplate).keys("comment:chapter:*");
        verify(redisTemplate, never()).delete(any(Set.class));
    }

    @Test
    void invalidateReviewCaches_ShouldDeleteReviewAndNovelCaches() {
        // Arrange
        Integer reviewId = 1;
        Set<String> reviewKeys = new HashSet<>();
        reviewKeys.add("review:novel:1");
        reviewKeys.add("review:novel:2");
        when(redisTemplate.keys("review:novel:*")).thenReturn(reviewKeys);

        // Act
        redisUtil.invalidateReviewCaches(reviewId);

        // Assert
        verify(redisTemplate).delete("review:1");
        verify(redisTemplate).keys("review:novel:*");
        verify(redisTemplate).delete(reviewKeys);
    }

    @Test
    void invalidateVoteCaches_ShouldDeleteVoteCaches() {
        // Arrange
        Integer novelId = 1;
        Set<String> voteKeys = new HashSet<>();
        voteKeys.add("vote:novel:1");
        Set<String> userVoteKeys = new HashSet<>();
        userVoteKeys.add("vote:user:user1");
        when(redisTemplate.keys("vote:novel:1")).thenReturn(voteKeys);
        when(redisTemplate.keys("vote:user:*")).thenReturn(userVoteKeys);

        // Act
        redisUtil.invalidateVoteCaches(novelId);

        // Assert
        verify(redisTemplate).keys("vote:novel:1");
        verify(redisTemplate).keys("vote:user:*");
        verify(redisTemplate).delete(voteKeys);
        verify(redisTemplate).delete(userVoteKeys);
    }

    @Test
    void invalidateEngagementCaches_ShouldDeleteEngagementCaches() {
        // Arrange
        Set<String> engagementKeys = new HashSet<>();
        engagementKeys.add("engagement:novel:1");
        engagementKeys.add("engagement:comment:1");
        when(redisTemplate.keys("engagement:*")).thenReturn(engagementKeys);

        // Act
        redisUtil.invalidateEngagementCaches();

        // Assert
        verify(redisTemplate).keys("engagement:*");
        verify(redisTemplate).delete(engagementKeys);
    }

    @Test
    void clearAllCaches_ShouldDeleteAllKeys() {
        // Arrange
        Set<String> allKeys = new HashSet<>();
        allKeys.add("key1");
        allKeys.add("key2");
        when(redisTemplate.keys("*")).thenReturn(allKeys);

        // Act
        redisUtil.clearAllCaches();

        // Assert
        verify(redisTemplate).keys("*");
        verify(redisTemplate).delete(allKeys);
    }

    @Test
    void clearAllCaches_WhenNoKeys_ShouldNotDelete() {
        // Arrange
        when(redisTemplate.keys("*")).thenReturn(new HashSet<>());

        // Act
        redisUtil.clearAllCaches();

        // Assert
        verify(redisTemplate).keys("*");
        verify(redisTemplate, never()).delete(any(Set.class));
    }
}
