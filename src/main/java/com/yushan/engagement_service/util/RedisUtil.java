package com.yushan.engagement_service.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis utility class for caching comment-related data in the engagement service.
 * Provides methods for caching comments, comment counts, and related queries.
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    // Cache key prefixes
    private static final String NOVEL_PREFIX = "novel:";
    private static final String VIEW_COUNT_PREFIX = "view_count:";
    private static final String POPULAR_PREFIX = "popular:";
    private static final String SEARCH_PREFIX = "search:";
    private static final String CATEGORY_PREFIX = "category:";

    // Cache TTL constants
    private static final Duration NOVEL_CACHE_TTL = Duration.ofHours(1);
    private static final Duration VIEW_COUNT_CACHE_TTL = Duration.ofMinutes(30);
    private static final Duration POPULAR_CACHE_TTL = Duration.ofMinutes(15);
    private static final Duration SEARCH_CACHE_TTL = Duration.ofMinutes(10);
    private static final Duration CATEGORY_CACHE_TTL = Duration.ofMinutes(30);

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

    // Novel-specific cache methods

    /**
     * Cache novel data
     */
    public void cacheNovel(Integer novelId, Object novelData) {
        String key = NOVEL_PREFIX + novelId;
        set(key, novelData, NOVEL_CACHE_TTL);
    }

    /**
     * Get cached novel data
     */
    public Object getCachedNovel(Integer novelId) {
        String key = NOVEL_PREFIX + novelId;
        return get(key);
    }

    /**
     * Get cached novel data with type casting
     */
    public <T> T getCachedNovel(Integer novelId, Class<T> clazz) {
        String key = NOVEL_PREFIX + novelId;
        return get(key, clazz);
    }

    /**
     * Delete novel cache
     */
    public void deleteNovelCache(Integer novelId) {
        String key = NOVEL_PREFIX + novelId;
        delete(key);
    }

    // View count cache methods

    /**
     * Cache view count for a novel
     */
    public void cacheViewCount(Integer novelId, Long viewCount) {
        String key = VIEW_COUNT_PREFIX + novelId;
        set(key, viewCount, VIEW_COUNT_CACHE_TTL);
    }

    /**
     * Get cached view count
     */
    public Long getCachedViewCount(Integer novelId) {
        String key = VIEW_COUNT_PREFIX + novelId;
        return get(key, Long.class);
    }

    /**
     * Increment cached view count
     */
    public Long incrementCachedViewCount(Integer novelId) {
        String key = VIEW_COUNT_PREFIX + novelId;
        Long newCount = increment(key);
        expire(key, VIEW_COUNT_CACHE_TTL);
        return newCount;
    }

    /**
     * Delete view count cache
     */
    public void deleteViewCountCache(Integer novelId) {
        String key = VIEW_COUNT_PREFIX + novelId;
        delete(key);
    }

    // Popular queries cache methods

    /**
     * Cache popular novels list
     */
    public void cachePopularNovels(String category, Object novelsData) {
        String key = POPULAR_PREFIX + "novels:" + category;
        set(key, novelsData, POPULAR_CACHE_TTL);
    }

    /**
     * Get cached popular novels
     */
    public Object getCachedPopularNovels(String category) {
        String key = POPULAR_PREFIX + "novels:" + category;
        return get(key);
    }

    /**
     * Get cached popular novels with type casting
     */
    public <T> T getCachedPopularNovels(String category, Class<T> clazz) {
        String key = POPULAR_PREFIX + "novels:" + category;
        return get(key, clazz);
    }

    /**
     * Cache search results
     */
    public void cacheSearchResults(String searchQuery, Object searchResults) {
        String key = SEARCH_PREFIX + "query:" + searchQuery.hashCode();
        set(key, searchResults, SEARCH_CACHE_TTL);
    }

    /**
     * Get cached search results
     */
    public Object getCachedSearchResults(String searchQuery) {
        String key = SEARCH_PREFIX + "query:" + searchQuery.hashCode();
        return get(key);
    }

    // Cache invalidation methods

    /**
     * Invalidate all novel-related caches
     */
    public void invalidateNovelCaches(Integer novelId) {
        deleteNovelCache(novelId);
        deleteViewCountCache(novelId);
        
        // Invalidate popular caches
        Set<String> popularKeys = keys(POPULAR_PREFIX + "*");
        if (!popularKeys.isEmpty()) {
            delete(popularKeys);
        }
    }

    /**
     * Invalidate all search caches
     */
    public void invalidateSearchCaches() {
        Set<String> searchKeys = keys(SEARCH_PREFIX + "*");
        if (!searchKeys.isEmpty()) {
            delete(searchKeys);
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

    // Category-specific cache methods

    /**
     * Cache category data
     */
    public void cacheCategory(Integer categoryId, Object categoryData) {
        String key = CATEGORY_PREFIX + "id:" + categoryId;
        set(key, categoryData, CATEGORY_CACHE_TTL);
    }

    /**
     * Get cached category data
     */
    public Object getCachedCategory(Integer categoryId) {
        String key = CATEGORY_PREFIX + "id:" + categoryId;
        return get(key);
    }

    /**
     * Get cached category data with type casting
     */
    public <T> T getCachedCategory(Integer categoryId, Class<T> clazz) {
        String key = CATEGORY_PREFIX + "id:" + categoryId;
        return get(key, clazz);
    }

    /**
     * Cache category by slug
     */
    public void cacheCategoryBySlug(String slug, Object categoryData) {
        String key = CATEGORY_PREFIX + "slug:" + slug;
        set(key, categoryData, CATEGORY_CACHE_TTL);
    }

    /**
     * Get cached category by slug
     */
    public Object getCachedCategoryBySlug(String slug) {
        String key = CATEGORY_PREFIX + "slug:" + slug;
        return get(key);
    }

    /**
     * Get cached category by slug with type casting
     */
    public <T> T getCachedCategoryBySlug(String slug, Class<T> clazz) {
        String key = CATEGORY_PREFIX + "slug:" + slug;
        return get(key, clazz);
    }

    /**
     * Cache categories list
     */
    public void cacheCategories(String type, Object categoriesData) {
        String key = CATEGORY_PREFIX + type;
        set(key, categoriesData, CATEGORY_CACHE_TTL);
    }

    /**
     * Get cached categories list
     */
    public Object getCachedCategories(String type) {
        String key = CATEGORY_PREFIX + type;
        return get(key);
    }

    /**
     * Get cached categories list with type casting
     */
    public <T> T getCachedCategories(String type, Class<T> clazz) {
        String key = CATEGORY_PREFIX + type;
        return get(key, clazz);
    }

    /**
     * Invalidate all category-related caches
     */
    public void invalidateCategoryCaches() {
        Set<String> categoryKeys = keys(CATEGORY_PREFIX + "*");
        if (!categoryKeys.isEmpty()) {
            delete(categoryKeys);
        }
    }
}
