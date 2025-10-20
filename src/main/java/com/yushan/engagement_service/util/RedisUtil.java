package com.yushan.engagement_service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis utility class for caching engagement-related data.
 * Provides methods for caching comments, reviews, votes and related queries.
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Cache key prefixes
    private static final String COMMENT_PREFIX = "comment:";
    private static final String REVIEW_PREFIX = "review:";
    private static final String VOTE_PREFIX = "vote:";
    private static final String LIKE_PREFIX = "like:";
    private static final String ENGAGEMENT_PREFIX = "engagement:";

    // Cache TTL constants
    private static final Duration COMMENT_CACHE_TTL = Duration.ofHours(1);
    private static final Duration REVIEW_CACHE_TTL = Duration.ofHours(2);
    private static final Duration VOTE_CACHE_TTL = Duration.ofHours(1);
    private static final Duration LIKE_CACHE_TTL = Duration.ofMinutes(30);
    private static final Duration ENGAGEMENT_CACHE_TTL = Duration.ofMinutes(15);

    /**
     * Set a key-value pair with TTL
     */
    public void set(String key, Object value, Duration ttl) {
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    /**
     * Set a key-value pair with TTL in seconds
     */
    public void set(String key, Object value, long ttlSeconds) {
        redisTemplate.opsForValue().set(key, value, ttlSeconds, TimeUnit.SECONDS);
    }

    /**
     * Get value by key
     */
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    /**
     * Get value by key with type casting
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, Class<T> clazz) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Delete key
     */
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    /**
     * Delete multiple keys
     */
    public void delete(Set<String> keys) {
        redisTemplate.delete(keys);
    }

    /**
     * Check if key exists
     */
    public boolean exists(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    /**
     * Increment a numeric value
     */
    public Long increment(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    /**
     * Increment a numeric value by delta
     */
    public Long increment(String key, long delta) {
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * Set expiration for existing key
     */
    public void expire(String key, Duration ttl) {
        redisTemplate.expire(key, ttl);
    }

    /**
     * Get keys matching pattern
     */
    public Set<String> keys(String pattern) {
        return redisTemplate.keys(pattern);
    }

    // Comment-specific cache methods

    /**
     * Cache comment data
     */
    public void cacheComment(Integer commentId, Object commentData) {
        String key = COMMENT_PREFIX + commentId;
        set(key, commentData, COMMENT_CACHE_TTL);
    }

    /**
     * Get cached comment data
     */
    public Object getCachedComment(Integer commentId) {
        String key = COMMENT_PREFIX + commentId;
        return get(key);
    }

    /**
     * Get cached comment data with type casting
     */
    public <T> T getCachedComment(Integer commentId, Class<T> clazz) {
        String key = COMMENT_PREFIX + commentId;
        return get(key, clazz);
    }

    /**
     * Cache comments for a chapter
     */
    public void cacheChapterComments(Integer chapterId, Object commentsData) {
        String key = COMMENT_PREFIX + "chapter:" + chapterId;
        set(key, commentsData, COMMENT_CACHE_TTL);
    }

    /**
     * Get cached comments for a chapter
     */
    public Object getCachedChapterComments(Integer chapterId) {
        String key = COMMENT_PREFIX + "chapter:" + chapterId;
        return get(key);
    }

    /**
     * Delete comment cache
     */
    public void deleteCommentCache(Integer commentId) {
        String key = COMMENT_PREFIX + commentId;
        delete(key);
    }

    // Review-specific cache methods

    /**
     * Cache review data
     */
    public void cacheReview(Integer reviewId, Object reviewData) {
        String key = REVIEW_PREFIX + reviewId;
        set(key, reviewData, REVIEW_CACHE_TTL);
    }

    /**
     * Get cached review data
     */
    public Object getCachedReview(Integer reviewId) {
        String key = REVIEW_PREFIX + reviewId;
        return get(key);
    }

    /**
     * Get cached review data with type casting
     */
    public <T> T getCachedReview(Integer reviewId, Class<T> clazz) {
        String key = REVIEW_PREFIX + reviewId;
        return get(key, clazz);
    }

    /**
     * Cache reviews for a novel
     */
    public void cacheNovelReviews(Integer novelId, Object reviewsData) {
        String key = REVIEW_PREFIX + "novel:" + novelId;
        set(key, reviewsData, REVIEW_CACHE_TTL);
    }

    /**
     * Get cached reviews for a novel
     */
    public Object getCachedNovelReviews(Integer novelId) {
        String key = REVIEW_PREFIX + "novel:" + novelId;
        return get(key);
    }

    /**
     * Delete review cache
     */
    public void deleteReviewCache(Integer reviewId) {
        String key = REVIEW_PREFIX + reviewId;
        delete(key);
    }

    // Vote-specific cache methods

    /**
     * Cache vote data
     */
    public void cacheVote(Integer voteId, Object voteData) {
        String key = VOTE_PREFIX + voteId;
        set(key, voteData, VOTE_CACHE_TTL);
    }

    /**
     * Get cached vote data
     */
    public Object getCachedVote(Integer voteId) {
        String key = VOTE_PREFIX + voteId;
        return get(key);
    }

    /**
     * Cache votes for a novel
     */
    public void cacheNovelVotes(Integer novelId, Object votesData) {
        String key = VOTE_PREFIX + "novel:" + novelId;
        set(key, votesData, VOTE_CACHE_TTL);
    }

    /**
     * Get cached votes for a novel
     */
    public Object getCachedNovelVotes(Integer novelId) {
        String key = VOTE_PREFIX + "novel:" + novelId;
        return get(key);
    }

    /**
     * Cache user votes
     */
    public void cacheUserVotes(String userId, Object votesData) {
        String key = VOTE_PREFIX + "user:" + userId;
        set(key, votesData, VOTE_CACHE_TTL);
    }

    /**
     * Get cached user votes
     */
    public Object getCachedUserVotes(String userId) {
        String key = VOTE_PREFIX + "user:" + userId;
        return get(key);
    }

    // Like-specific cache methods

    /**
     * Cache like count for comments/reviews
     */
    public void cacheLikeCount(String entityType, Integer entityId, Long likeCount) {
        String key = LIKE_PREFIX + entityType + ":" + entityId;
        set(key, likeCount, LIKE_CACHE_TTL);
    }

    /**
     * Get cached like count
     */
    public Long getCachedLikeCount(String entityType, Integer entityId) {
        String key = LIKE_PREFIX + entityType + ":" + entityId;
        return get(key, Long.class);
    }

    /**
     * Increment cached like count
     */
    public Long incrementCachedLikeCount(String entityType, Integer entityId) {
        String key = LIKE_PREFIX + entityType + ":" + entityId;
        Long newCount = increment(key);
        expire(key, LIKE_CACHE_TTL);
        return newCount;
    }

    /**
     * Decrement cached like count
     */
    public Long decrementCachedLikeCount(String entityType, Integer entityId) {
        String key = LIKE_PREFIX + entityType + ":" + entityId;
        Long newCount = increment(key, -1);
        expire(key, LIKE_CACHE_TTL);
        return newCount;
    }

    // Engagement statistics cache methods

    /**
     * Cache engagement statistics
     */
    public void cacheEngagementStats(String entityType, Integer entityId, Object statsData) {
        String key = ENGAGEMENT_PREFIX + entityType + ":" + entityId;
        set(key, statsData, ENGAGEMENT_CACHE_TTL);
    }

    /**
     * Get cached engagement statistics
     */
    public Object getCachedEngagementStats(String entityType, Integer entityId) {
        String key = ENGAGEMENT_PREFIX + entityType + ":" + entityId;
        return get(key);
    }

    // Cache invalidation methods

    /**
     * Invalidate all comment-related caches
     */
    public void invalidateCommentCaches(Integer commentId) {
        deleteCommentCache(commentId);
        
        // Invalidate chapter comment caches
        Set<String> commentKeys = keys(COMMENT_PREFIX + "chapter:*");
        if (!commentKeys.isEmpty()) {
            delete(commentKeys);
        }
    }

    /**
     * Invalidate all review-related caches
     */
    public void invalidateReviewCaches(Integer reviewId) {
        deleteReviewCache(reviewId);
        
        // Invalidate novel review caches
        Set<String> reviewKeys = keys(REVIEW_PREFIX + "novel:*");
        if (!reviewKeys.isEmpty()) {
            delete(reviewKeys);
        }
    }

    /**
     * Invalidate all vote-related caches
     */
    public void invalidateVoteCaches(Integer novelId) {
        Set<String> voteKeys = keys(VOTE_PREFIX + "novel:" + novelId);
        if (!voteKeys.isEmpty()) {
            delete(voteKeys);
        }
        
        // Invalidate user vote caches
        Set<String> userVoteKeys = keys(VOTE_PREFIX + "user:*");
        if (!userVoteKeys.isEmpty()) {
            delete(userVoteKeys);
        }
    }

    /**
     * Invalidate all engagement caches
     */
    public void invalidateEngagementCaches() {
        Set<String> engagementKeys = keys(ENGAGEMENT_PREFIX + "*");
        if (!engagementKeys.isEmpty()) {
            delete(engagementKeys);
        }
    }

    /**
     * Clear all caches (use with caution)
     */
    public void clearAllCaches() {
        Set<String> allKeys = keys("*");
        if (!allKeys.isEmpty()) {
            delete(allKeys);
        }
    }
}
