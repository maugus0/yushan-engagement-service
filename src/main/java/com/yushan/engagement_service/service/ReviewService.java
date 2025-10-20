package com.yushan.engagement_service.service;

import com.yushan.engagement_service.dao.ReviewMapper;
import com.yushan.engagement_service.dto.review.*;
import com.yushan.engagement_service.dto.common.*;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
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
import com.yushan.engagement_service.client.GamificationServiceClient;

@Service
public class ReviewService {

    @Autowired
    private ReviewMapper reviewMapper;

    @Autowired
    private ContentServiceClient contentServiceClient;

    @Autowired
    private UserServiceClient userServiceClient;

    @Autowired
    private GamificationServiceClient gamificationServiceClient;

    @Autowired
    private KafkaEventProducerService kafkaEventProducerService;

    /**
     * Create a new review
     * Checks if user already reviewed the novel
     */
    @Transactional
    public ReviewResponseDTO createReview(UUID userId, ReviewCreateRequestDTO request) {
        // check novel if exists（by content service client）
        if (!contentServiceClient.novelExists(request.getNovelId())) {
            throw new ResourceNotFoundException("Novel not found");
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

        // add exp
        // Add EXP via gamification service
        gamificationServiceClient.addExpForReview(userId);

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
        
        if (request.getRating() != null && !request.getRating().equals(existingReview.getRating())) {
            existingReview.setRating(request.getRating());
            hasChanges = true;
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

        int result = reviewMapper.deleteByPrimaryKey(reviewId);

        return result > 0;
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
        reviewMapper.updateLikeCount(reviewId, increment);
        review = reviewMapper.selectByPrimaryKey(reviewId);

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

        // get username（by user service client）
        dto.setUsername(userServiceClient.getUsernameById(review.getUserId()));

        // get novel title（by content service client）
        ApiResponse<NovelDetailResponseDTO> novelResponse = contentServiceClient.getNovelById(review.getNovelId());
        dto.setNovelTitle(novelResponse != null && novelResponse.getData() != null ? novelResponse.getData().getTitle() : "Novel not found");

        return dto;
    }
}