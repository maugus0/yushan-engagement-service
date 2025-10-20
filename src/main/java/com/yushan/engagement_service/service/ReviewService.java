package com.yushan.engagement_service.service;

import com.yushan.engagement_service.dao.ReviewMapper;
import com.yushan.engagement_service.dto.review.*;
import com.yushan.engagement_service.dto.common.*;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import com.yushan.engagement_service.dto.review.NovelRatingStatsDTO;
import com.yushan.engagement_service.entity.Review;
import com.yushan.engagement_service.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import com.yushan.engagement_service.client.ContentServiceClient;
import com.yushan.engagement_service.client.UserServiceClient;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ContentServiceClient contentServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private KafkaEventProducerService kafkaEventProducerService;

    /**
     * Create a new review
     * Checks if user already reviewed the novel
     */
    @Transactional
    public ReviewResponseDTO createReview(UUID userId, ReviewCreateRequestDTO request) {
        ApiResponse<NovelDetailResponseDTO> novelResp = contentServiceClient.getNovelById(request.getNovelId());
        if (novelResp == null || novelResp.getData() == null) {
            throw new ResourceNotFoundException("Novel does not exist: " + request.getNovelId());
        }

        // check if user has already reviewed the novel
        Review existingReview = reviewMapper.selectByUserAndNovel(userId, request.getNovelId());
        if (existingReview != null) {
            throw new IllegalArgumentException("You have already reviewed this novel");
        }

        // add comment
        Review review = new Review();
        review.setUuid(UUID.randomUUID());
        review.setUserId(userId);
        review.setNovelId(request.getNovelId());
        review.setRating(request.getRating());
        review.setTitle(request.getTitle());
        review.setContent(request.getContent());
        review.setLikeCnt(0);
        review.setIsSpoiler(request.getIsSpoiler() != null ? request.getIsSpoiler() : false);
        Date now = new Date();
        review.setCreateTime(now);
        review.setUpdateTime(now);

        reviewMapper.insertSelective(review);

        // Update novel rating and review count
        updateNovelRatingAndCount(request.getNovelId());

        // TODO: Kafka need to push a event here to gamification
        // {
        //     "eventType": "USER_REVIEW",
        //     "userId": "b2c3d4e5-f6a7-8901-2345-67890abcdef1",
        //     "entityId": 202,
        //     "timestamp": "2025-10-21T11:45:10.123+08:00"
        //   }

        return toResponseDTO(review);
    }

    /**
     * Update an existing review
     * Only the author of the review can update it
     */
    @Transactional
    public ReviewResponseDTO updateReview(Integer reviewId, UUID userId, ReviewUpdateRequestDTO request) {
        Review existingReview = reviewMapper.selectByPrimaryKey(reviewId);
        if (existingReview == null) {
            throw new ResourceNotFoundException("Review not found");
        }

        // Check if user is the author of the review
        if (!existingReview.getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only update your own reviews");
        }

        // Update fields if provided
        boolean hasChanges = false;
        boolean ratingChanged = false;
        
        if (request.getRating() != null && !request.getRating().equals(existingReview.getRating())) {
            existingReview.setRating(request.getRating());
            hasChanges = true;
            ratingChanged = true;
        }
        if (request.getTitle() != null && !request.getTitle().trim().isEmpty()) {
            existingReview.setTitle(request.getTitle().trim());
            hasChanges = true;
        }
        if (request.getContent() != null && !request.getContent().trim().isEmpty()) {
            existingReview.setContent(request.getContent().trim());
            hasChanges = true;
        }
        if (request.getIsSpoiler() != null && !request.getIsSpoiler().equals(existingReview.getIsSpoiler())) {
            existingReview.setIsSpoiler(request.getIsSpoiler());
            hasChanges = true;
        }

        if (hasChanges) {
            existingReview.setUpdateTime(new Date());
            reviewMapper.updateByPrimaryKeySelective(existingReview);

            // Only update novel rating if rating changed
            if (ratingChanged) {
                updateNovelRatingAndCount(existingReview.getNovelId());
            }
        }

        return toResponseDTO(existingReview);
    }

    /**
     * Delete a review
     * Only the author of the review or admin can delete it
     */
    @Transactional
    public boolean deleteReview(Integer reviewId, UUID userId, boolean isAdmin) {
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new ResourceNotFoundException("Review not found");
        }

        // Check if user is the author or admin
        if (!review.getUserId().equals(userId) && !isAdmin) {
            throw new IllegalArgumentException("You can only delete your own reviews");
        }

        Integer novelId = review.getNovelId();
        int result = reviewMapper.deleteByPrimaryKey(reviewId);

        if (result > 0) {
            // Update novel rating and review count
            updateNovelRatingAndCount(novelId);
            return true;
        }
        return false;
    }

    /**
     * Get review by ID
     */
    public ReviewResponseDTO getReview(Integer reviewId) {
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new ResourceNotFoundException("Review not found");
        }
        return toResponseDTO(review);
    }

    /**
     * Get reviews for a specific novel with pagination
     */
    public PageResponseDTO<ReviewResponseDTO> getReviewsByNovel(Integer novelId, int page, int size, String sort, String order) {
        // Validate and set defaults
        if (page < 0) page = 0;
        if (size <= 0) size = 10;
        if (size > 100) size = 100;
        if (sort == null || sort.trim().isEmpty()) sort = "createTime";
        if (order == null || (!order.equalsIgnoreCase("asc") && !order.equalsIgnoreCase("desc"))) {
            order = "desc";
        }

        ReviewSearchRequestDTO request = new ReviewSearchRequestDTO(page, size, sort, order, novelId, null, null, null);
        List<Review> reviews = reviewMapper.selectReviewsWithPagination(request);
        long totalElements = reviewMapper.countReviews(request);

        List<ReviewResponseDTO> reviewDTOs = reviews.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(reviewDTOs, totalElements, page, size);
    }

    /**
     * Get all reviews with pagination and filtering
     */
    public PageResponseDTO<ReviewResponseDTO> getAllReviews(ReviewSearchRequestDTO request) {
        // Validate and set defaults
        if (request.getPage() == null || request.getPage() < 0) {
            request.setPage(0);
        }
        if (request.getSize() == null || request.getSize() <= 0) {
            request.setSize(10);
        }
        if (request.getSize() > 100) {
            request.setSize(100);
        }
        if (request.getSort() == null || request.getSort().trim().isEmpty()) {
            request.setSort("createTime");
        }
        if (request.getOrder() == null || (!request.getOrder().equalsIgnoreCase("asc") && !request.getOrder().equalsIgnoreCase("desc"))) {
            request.setOrder("desc");
        }

        List<Review> reviews = reviewMapper.selectReviewsWithPagination(request);
        long totalElements = reviewMapper.countReviews(request);

        List<ReviewResponseDTO> reviewDTOs = reviews.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());

        return new PageResponseDTO<>(reviewDTOs, totalElements, request.getPage(), request.getSize());
    }

    /**
     * Toggle like on a review (increment or decrement like count)
     * In a real application, you would need a separate table to track user likes
     */
    @Transactional
    public ReviewResponseDTO toggleLike(Integer reviewId, UUID currentUserId, boolean isLiking) {
        Review review = reviewMapper.selectByPrimaryKey(reviewId);
        if (review == null) {
            throw new ResourceNotFoundException("Review not found");
        }

        // Increment or decrement like count
        int increment = isLiking ? 1 : -1;
        int result = reviewMapper.updateLikeCount(reviewId, increment);

        if (result > 0) {
            // Fetch updated review
            review = reviewMapper.selectByPrimaryKey(reviewId);
        }

        return toResponseDTO(review);
    }

    /**
     * Get user's reviews
     */
    public List<ReviewResponseDTO> getUserReviews(UUID userId) {
        List<Review> reviews = reviewMapper.selectByUserId(userId);
        return reviews.stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Check if user has reviewed a novel
     */
    public boolean hasUserReviewedNovel(UUID userId, Integer novelId) {
        Review review = reviewMapper.selectByUserAndNovel(userId, novelId);
        return review != null;
    }

    /**
     * Get user's review for a specific novel
     */
    public ReviewResponseDTO getUserReviewForNovel(UUID userId, Integer novelId) {
        Review review = reviewMapper.selectByUserAndNovel(userId, novelId);
        if (review == null) {
            throw new ResourceNotFoundException("Review not found");
        }
        return toResponseDTO(review);
    }


    /**
     * Convert Review entity to ReviewResponseDTO
     */
    private ReviewResponseDTO toResponseDTO(Review review) {
        ReviewResponseDTO dto = new ReviewResponseDTO();
        dto.setId(review.getId());
        dto.setUuid(review.getUuid());
        dto.setUserId(review.getUserId());
        dto.setNovelId(review.getNovelId());
        dto.setRating(review.getRating());
        dto.setTitle(review.getTitle());
        dto.setContent(review.getContent());
        dto.setLikeCnt(review.getLikeCnt());
        dto.setIsSpoiler(review.getIsSpoiler());
        dto.setCreateTime(review.getCreateTime());
        dto.setUpdateTime(review.getUpdateTime());

        // Get username from UserServiceClient
        try {
            String username = userServiceClient.getUsernameById(review.getUserId());
            dto.setUsername(username);
        } catch (Exception e) {
            dto.setUsername(null);
        }
        
        // Get novel title from ContentServiceClient
        try {
            ApiResponse<NovelDetailResponseDTO> novelDetail = contentServiceClient.getNovelById(review.getNovelId());
            dto.setNovelTitle(novelDetail.getData().getTitle());
        } catch (ResourceNotFoundException e) {
            dto.setNovelTitle("Novel not found");
        } catch (Exception e) {
            dto.setNovelTitle(null);
        }
        
        return dto;
    }

    /**
     * Update novel's average rating and review count
     * This method calculates the statistics and calls NovelService
     */
    private void updateNovelRatingAndCount(Integer novelId) {
        // Get all reviews for this novel
        List<Review> reviews = reviewMapper.selectByNovelId(novelId);
        
        float avgRating;
        int reviewCount;
        
        if (reviews.isEmpty()) {
            // No reviews, set to default values
            avgRating = 0.0f;
            reviewCount = 0;
        } else {
            // Calculate average rating
            double totalRating = reviews.stream()
                    .mapToInt(Review::getRating)
                    .sum();
            avgRating = (float) (totalRating / reviews.size());
            
            // Round to 1 decimal place
            avgRating = Math.round(avgRating * 10.0f) / 10.0f;
            reviewCount = reviews.size();
        }
        
        // Call contentServiceClient with calculated values
        contentServiceClient.updateNovelRatingAndCount(novelId, avgRating, reviewCount);
    }

    /**
     * Get detailed novel rating statistics
     */
    public NovelRatingStatsDTO getNovelRatingStats(Integer novelId) {
        // Get novel basic info through contentServiceClient
        ApiResponse<NovelDetailResponseDTO> novelDetail = contentServiceClient.getNovelById(novelId);

        List<Review> reviews = reviewMapper.selectByNovelId(novelId);
        
        NovelRatingStatsDTO stats = new NovelRatingStatsDTO();
        stats.setNovelId(novelId);
        stats.setNovelTitle(novelDetail.getData().getTitle());
        stats.setTotalReviews(reviews.size());
        stats.setAverageRating(novelDetail.getData().getAvgRating());
        
        if (!reviews.isEmpty()) {
            // Calculate rating distribution
            long rating5 = reviews.stream().mapToInt(r -> r.getRating() == 5 ? 1 : 0).sum();
            long rating4 = reviews.stream().mapToInt(r -> r.getRating() == 4 ? 1 : 0).sum();
            long rating3 = reviews.stream().mapToInt(r -> r.getRating() == 3 ? 1 : 0).sum();
            long rating2 = reviews.stream().mapToInt(r -> r.getRating() == 2 ? 1 : 0).sum();
            long rating1 = reviews.stream().mapToInt(r -> r.getRating() == 1 ? 1 : 0).sum();
            
            stats.setRating5Count((int) rating5);
            stats.setRating4Count((int) rating4);
            stats.setRating3Count((int) rating3);
            stats.setRating2Count((int) rating2);
            stats.setRating1Count((int) rating1);
            
            // Calculate percentages
            int total = reviews.size();
            stats.setRating5Percentage((float) rating5 / total * 100);
            stats.setRating4Percentage((float) rating4 / total * 100);
            stats.setRating3Percentage((float) rating3 / total * 100);
            stats.setRating2Percentage((float) rating2 / total * 100);
            stats.setRating1Percentage((float) rating1 / total * 100);
        }
        
        return stats;
    }
}