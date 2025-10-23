package com.yushan.engagement_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushan.engagement_service.dto.review.ReviewCreateRequestDTO;
import com.yushan.engagement_service.dto.review.ReviewResponseDTO;
import com.yushan.engagement_service.dto.review.ReviewUpdateRequestDTO;
import com.yushan.engagement_service.dto.review.NovelRatingStatsDTO;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.service.ReviewService;
import com.yushan.engagement_service.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private ObjectMapper objectMapper = new ObjectMapper();
    private CustomUserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        // Setup mock user
        userDetails = new CustomUserDetails(
            UUID.fromString("550e8400-e29b-41d4-a716-446655440001").toString(),
            "test@example.com",
            "testuser",
            "USER",
            0
        );

        authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void createReview_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        ReviewCreateRequestDTO request = new ReviewCreateRequestDTO();
        request.setNovelId(1);
        request.setRating(5);
        request.setTitle("Great Novel");
        request.setContent("Great novel!");

        ReviewResponseDTO mockResponse = new ReviewResponseDTO();
        mockResponse.setId(1);
        mockResponse.setUserId(testUserId);
        mockResponse.setNovelId(1);
        mockResponse.setRating(5);
        mockResponse.setContent("Great novel!");
        mockResponse.setLikeCnt(0);
        mockResponse.setCreateTime(new Date());

        when(reviewService.createReview(eq(testUserId), any(ReviewCreateRequestDTO.class)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(post("/api/v1/reviews")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Review created successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.content").value("Great novel!"));
    }

    @Test
    void updateReview_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Integer testReviewId = 1;
        ReviewUpdateRequestDTO request = new ReviewUpdateRequestDTO();
        request.setRating(4);
        request.setTitle("Updated Title");
        request.setContent("Updated review content.");

        ReviewResponseDTO mockResponse = new ReviewResponseDTO();
        mockResponse.setId(testReviewId);
        mockResponse.setUserId(testUserId);
        mockResponse.setRating(4);
        mockResponse.setContent("Updated review content.");
        mockResponse.setLikeCnt(5);
        mockResponse.setCreateTime(new Date());

        when(reviewService.updateReview(eq(testReviewId), eq(testUserId), any(ReviewUpdateRequestDTO.class)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(put("/api/v1/reviews/{reviewId}", testReviewId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Review updated successfully"))
                .andExpect(jsonPath("$.data.id").value(testReviewId))
                .andExpect(jsonPath("$.data.content").value("Updated review content."));
    }

    @Test
    void deleteReview_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Integer testReviewId = 1;

        // Mock service call to return true (successful deletion)
        when(reviewService.deleteReview(eq(testReviewId), eq(testUserId), anyBoolean()))
                .thenReturn(true);

        // Execute & Verify
        mockMvc.perform(delete("/api/v1/reviews/{reviewId}", testReviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Review deleted successfully"));
    }

    @Test
    void getReview_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testReviewId = 1;
        
        ReviewResponseDTO mockResponse = new ReviewResponseDTO();
        mockResponse.setId(testReviewId);
        mockResponse.setContent("Test review");
        mockResponse.setRating(5);

        when(reviewService.getReview(eq(testReviewId))).thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/reviews/{reviewId}", testReviewId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Review retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(testReviewId))
                .andExpect(jsonPath("$.data.content").value("Test review"));
    }

    @Test
    void getReviewsByNovel_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testNovelId = 1;
        
        ReviewResponseDTO review1 = new ReviewResponseDTO();
        review1.setId(1);
        review1.setContent("Review 1");
        review1.setRating(5);
        
        List<ReviewResponseDTO> mockReviews = new ArrayList<>();
        mockReviews.add(review1);

        PageResponseDTO<ReviewResponseDTO> mockPageResponse = new PageResponseDTO<>();
        mockPageResponse.setContent(mockReviews);
        mockPageResponse.setCurrentPage(0);
        mockPageResponse.setSize(10);
        mockPageResponse.setTotalElements(1L);
        mockPageResponse.setTotalPages(1);
        
        when(reviewService.getReviewsByNovel(eq(testNovelId), eq(0), eq(10), eq("createTime"), eq("desc")))
                .thenReturn(mockPageResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/reviews/novel/{novelId}", testNovelId)
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Reviews retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].content").value("Review 1"));
    }

    @Test
    void toggleLike_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Integer testReviewId = 1;

        ReviewResponseDTO mockResponse = new ReviewResponseDTO();
        mockResponse.setId(testReviewId);
        mockResponse.setLikeCnt(1);

        when(reviewService.toggleLike(eq(testReviewId), eq(testUserId), eq(true)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(post("/api/v1/reviews/{reviewId}/like", testReviewId)
                .principal(() -> testUserId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Review liked successfully"))
                .andExpect(jsonPath("$.data.id").value(testReviewId))
                .andExpect(jsonPath("$.data.likeCnt").value(1));
    }

    @Test
    void getNovelRatingStats_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup - Create ADMIN user for this test
        CustomUserDetails adminUserDetails = new CustomUserDetails(
            UUID.fromString("550e8400-e29b-41d4-a716-446655440001").toString(),
            "admin@example.com",
            "admin",
            "ADMIN",
            0
        );
        
        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
            adminUserDetails, null, Collections.singletonList(new SimpleGrantedAuthority("ROLE_ADMIN"))
        );
        SecurityContextHolder.getContext().setAuthentication(adminAuth);
        
        Integer testNovelId = 1;
        
        NovelRatingStatsDTO mockStats = new NovelRatingStatsDTO();
        mockStats.setNovelId(testNovelId);
        mockStats.setAverageRating(4.5f);
        mockStats.setTotalReviews(10);

        when(reviewService.getNovelRatingStats(eq(testNovelId))).thenReturn(mockStats);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/reviews/novel/{novelId}/rating-stats", testNovelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Novel rating statistics retrieved"))
                .andExpect(jsonPath("$.data.averageRating").value(4.5))
                .andExpect(jsonPath("$.data.totalReviews").value(10));
    }
}
