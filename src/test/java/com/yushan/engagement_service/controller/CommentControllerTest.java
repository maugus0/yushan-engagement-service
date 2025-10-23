package com.yushan.engagement_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yushan.engagement_service.dto.comment.*;
import com.yushan.engagement_service.service.CommentService;
import com.yushan.engagement_service.security.CustomUserDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Unit tests for CommentController
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;

    private ObjectMapper objectMapper;
    private CustomUserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

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
    void createComment_Success() throws Exception {
        // Arrange
        CommentCreateRequestDTO request = new CommentCreateRequestDTO();
        request.setChapterId(1);
        request.setContent("Test comment");

        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
            .id(1)
            .content("Test comment")
            .userId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
            .chapterId(1)
            .build();

        when(commentService.createComment(any(UUID.class), any(CommentCreateRequestDTO.class)))
            .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(authentication))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment created successfully"))
                .andExpect(jsonPath("$.data.content").value("Test comment"));

        verify(commentService).createComment(any(UUID.class), any(CommentCreateRequestDTO.class));
    }

    @Test
    void updateComment_Success() throws Exception {
        // Arrange
        Integer commentId = 1;
        CommentUpdateRequestDTO request = new CommentUpdateRequestDTO();
        request.setContent("Updated comment");

        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
            .id(commentId)
            .content("Updated comment")
            .userId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"))
            .build();

        when(commentService.updateComment(anyInt(), any(UUID.class), any(CommentUpdateRequestDTO.class)))
            .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(put("/api/v1/comments/{id}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").value("Updated comment"));

        verify(commentService).updateComment(anyInt(), any(UUID.class), any(CommentUpdateRequestDTO.class));
    }

    @Test
    void deleteComment_Success() throws Exception {
        // Arrange
        Integer commentId = 1;

        when(commentService.deleteComment(anyInt(), any(UUID.class), anyBoolean()))
            .thenReturn(true);

        // Act & Assert
        mockMvc.perform(delete("/api/v1/comments/{id}", commentId)
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment deleted successfully"));

        verify(commentService).deleteComment(anyInt(), any(UUID.class), anyBoolean());
    }

    @Test
    void likeComment_Success() throws Exception {
        // Arrange
        Integer commentId = 1;

        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
            .id(commentId)
            .likeCnt(1)
            .build();

        when(commentService.toggleLike(anyInt(), any(UUID.class), anyBoolean()))
            .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments/{id}/like", commentId)
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment liked successfully"));

        verify(commentService).toggleLike(anyInt(), any(UUID.class), anyBoolean());
    }

    @Test
    void unlikeComment_Success() throws Exception {
        // Arrange
        Integer commentId = 1;

        CommentResponseDTO responseDTO = CommentResponseDTO.builder()
            .id(commentId)
            .likeCnt(0)
            .build();

        when(commentService.toggleLike(anyInt(), any(UUID.class), anyBoolean()))
            .thenReturn(responseDTO);

        // Act & Assert
        mockMvc.perform(post("/api/v1/comments/{id}/unlike", commentId)
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment unliked successfully"));

        verify(commentService).toggleLike(anyInt(), any(UUID.class), anyBoolean());
    }

    @Test
    void getCommentsByChapter_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testChapterId = 1;
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("Comment 1");
        
        CommentListResponseDTO mockResponse = new CommentListResponseDTO();
        mockResponse.setComments(Collections.singletonList(comment1));
        mockResponse.setTotalCount(1L);
        mockResponse.setTotalPages(1);
        mockResponse.setCurrentPage(0);
        mockResponse.setPageSize(10);

        when(commentService.getCommentsByChapter(eq(testChapterId), eq(testUserId), eq(0), eq(10), eq("createTime"), eq("desc")))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/chapter/{chapterId}", testChapterId)
                .param("page", "0")
                .param("size", "10")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comments retrieved successfully"))
                .andExpect(jsonPath("$.data.comments[0].id").value(1))
                .andExpect(jsonPath("$.data.comments[0].content").value("Comment 1"));
    }

    @Test
    void getUserComments_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("My comment");
        
        when(commentService.getUserComments(eq(testUserId)))
                .thenReturn(Collections.singletonList(comment1));

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/my-comments")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Your comments retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].content").value("My comment"));
    }

    @Test
    void getChapterCommentStats_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testChapterId = 1;
        
        CommentStatisticsDTO mockStats = new CommentStatisticsDTO();
        mockStats.setChapterId(testChapterId);
        mockStats.setTotalComments(5);
        mockStats.setAvgLikesPerComment(2);

        when(commentService.getChapterCommentStats(eq(testChapterId))).thenReturn(mockStats);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/chapter/{chapterId}/statistics", testChapterId)
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment statistics retrieved"))
                .andExpect(jsonPath("$.data.totalComments").value(5))
                .andExpect(jsonPath("$.data.avgLikesPerComment").value(2));
    }

    // ========================================
    // ADDITIONAL TEST CASES FOR COVERAGE
    // ========================================

    @Test
    void getComment_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testCommentId = 1;
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        CommentResponseDTO mockComment = new CommentResponseDTO();
        mockComment.setId(testCommentId);
        mockComment.setContent("Test comment");
        mockComment.setUserId(testUserId);

        when(commentService.getComment(eq(testCommentId), eq(testUserId))).thenReturn(mockComment);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/{id}", testCommentId)
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(testCommentId))
                .andExpect(jsonPath("$.data.content").value("Test comment"));
    }

    @Test
    void getComment_WithoutAuthentication_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testCommentId = 1;
        
        CommentResponseDTO mockComment = new CommentResponseDTO();
        mockComment.setId(testCommentId);
        mockComment.setContent("Test comment");

        when(commentService.getComment(eq(testCommentId), isNull())).thenReturn(mockComment);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/{id}", testCommentId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment retrieved successfully"));
    }

    @Test
    void getCommentsByNovel_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testNovelId = 1;
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("Comment 1");
        
        CommentListResponseDTO mockResponse = new CommentListResponseDTO();
        mockResponse.setComments(Collections.singletonList(comment1));
        mockResponse.setTotalCount(1L);

        when(commentService.getCommentsByNovel(eq(testNovelId), eq(testUserId), any(CommentSearchRequestDTO.class)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/novel/{novelId}", testNovelId)
                .param("page", "0")
                .param("size", "10")
                .param("isSpoiler", "false")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comments retrieved successfully"))
                .andExpect(jsonPath("$.data.comments[0].id").value(1));
    }

    @Test
    void hasUserCommentedOnChapter_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testChapterId = 1;
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");

        when(commentService.hasUserCommentedOnChapter(eq(testUserId), eq(testChapterId)))
                .thenReturn(true);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/check/chapter/{chapterId}", testChapterId)
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment status checked"))
                .andExpect(jsonPath("$.data").value(true));
    }

    // ========================================
    // ADMIN ENDPOINT TESTS
    // ========================================

    @Test
    void getAllCommentsForModeration_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("Comment 1");
        
        CommentListResponseDTO mockResponse = new CommentListResponseDTO();
        mockResponse.setComments(Collections.singletonList(comment1));
        mockResponse.setTotalCount(1L);

        when(commentService.getAllComments(any(CommentSearchRequestDTO.class), any(UUID.class)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/admin/moderation")
                .param("page", "0")
                .param("size", "10")
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("All comments retrieved successfully"))
                .andExpect(jsonPath("$.data.comments[0].id").value(1));
    }

    @Test
    void getAllCommentsAdmin_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("Comment 1");
        
        CommentListResponseDTO mockResponse = new CommentListResponseDTO();
        mockResponse.setComments(Collections.singletonList(comment1));
        mockResponse.setTotalCount(1L);

        when(commentService.getAllComments(any(CommentSearchRequestDTO.class), any(UUID.class)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/admin/all")
                .param("page", "0")
                .param("size", "10")
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("All comments retrieved successfully"))
                .andExpect(jsonPath("$.data.comments[0].id").value(1));
    }

    @Test
    void searchCommentsAdmin_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("Comment 1");
        
        CommentListResponseDTO mockResponse = new CommentListResponseDTO();
        mockResponse.setComments(Collections.singletonList(comment1));
        mockResponse.setTotalCount(1L);

        when(commentService.getAllComments(any(CommentSearchRequestDTO.class), any(UUID.class)))
                .thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/admin/search")
                .param("page", "0")
                .param("size", "10")
                .param("search", "test")
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comments search completed"))
                .andExpect(jsonPath("$.data.comments[0].id").value(1));
    }

    @Test
    void getUserCommentsAdmin_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        String targetUserId = "550e8400-e29b-41d4-a716-446655440002";
        
        CommentResponseDTO comment1 = new CommentResponseDTO();
        comment1.setId(1);
        comment1.setContent("User comment");
        
        when(commentService.getUserComments(eq(UUID.fromString(targetUserId))))
                .thenReturn(Collections.singletonList(comment1));

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/admin/user/{userId}", targetUserId)
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User comments retrieved successfully"))
                .andExpect(jsonPath("$.data[0].id").value(1))
                .andExpect(jsonPath("$.data[0].content").value("User comment"));
    }

    @Test
    void getModerationStats_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        CommentModerationStatsDTO mockStats = new CommentModerationStatsDTO();
        mockStats.setTotalComments(100L);
        mockStats.setSpoilerComments(20L);
        mockStats.setNonSpoilerComments(80L);
        mockStats.setCommentsToday(5L);

        when(commentService.getModerationStatistics()).thenReturn(mockStats);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/comments/admin/statistics")
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Moderation statistics retrieved"))
                .andExpect(jsonPath("$.data.totalComments").value(100))
                .andExpect(jsonPath("$.data.spoilerComments").value(20))
                .andExpect(jsonPath("$.data.commentsToday").value(5));
    }

    @Test
    void deleteCommentAdmin_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        Integer testCommentId = 1;

        when(commentService.deleteComment(eq(testCommentId), isNull(), eq(true)))
                .thenReturn(true);

        // Execute & Verify
        mockMvc.perform(delete("/api/v1/comments/admin/{id}", testCommentId)
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment deleted successfully by admin"));
    }

    @Test
    void deleteCommentAdmin_WithFailure_ShouldReturnError() throws Exception {
        // Setup ADMIN user
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
        
        Integer testCommentId = 1;

        when(commentService.deleteComment(eq(testCommentId), isNull(), eq(true)))
                .thenReturn(false);

        // Execute & Verify
        mockMvc.perform(delete("/api/v1/comments/admin/{id}", testCommentId)
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value("Failed to delete comment"));
    }

    @Test
    void batchDeleteComments_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        CommentBatchDeleteRequestDTO request = new CommentBatchDeleteRequestDTO();
        request.setCommentIds(Arrays.asList(1, 2, 3));

        when(commentService.batchDeleteComments(any(CommentBatchDeleteRequestDTO.class), eq(true)))
                .thenReturn(3);

        // Execute & Verify
        mockMvc.perform(post("/api/v1/comments/admin/batch-delete")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Successfully deleted 3 comment(s)"));
    }

    @Test
    void deleteAllUserComments_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        String targetUserId = "550e8400-e29b-41d4-a716-446655440002";

        when(commentService.deleteAllUserComments(eq(UUID.fromString(targetUserId))))
                .thenReturn(5);

        // Execute & Verify
        mockMvc.perform(delete("/api/v1/comments/admin/user/{userId}/all", targetUserId)
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Successfully deleted 5 comment(s) from user"));
    }

    @Test
    void deleteAllChapterComments_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        Integer testChapterId = 1;

        when(commentService.deleteAllChapterComments(eq(testChapterId)))
                .thenReturn(10);

        // Execute & Verify
        mockMvc.perform(delete("/api/v1/comments/admin/chapter/{chapterId}/all", testChapterId)
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Successfully deleted 10 comment(s) from chapter"));
    }

    @Test
    void bulkUpdateSpoilerStatus_WithAdminUser_ShouldReturnSuccess() throws Exception {
        // Setup ADMIN user
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
        
        CommentBulkSpoilerUpdateRequestDTO request = new CommentBulkSpoilerUpdateRequestDTO();
        request.setCommentIds(Arrays.asList(1, 2, 3));
        request.setIsSpoiler(true);

        when(commentService.bulkUpdateSpoilerStatus(any(CommentBulkSpoilerUpdateRequestDTO.class)))
                .thenReturn(3);

        // Execute & Verify
        mockMvc.perform(patch("/api/v1/comments/admin/bulk-spoiler")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .principal(adminAuth))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Successfully updated 3 comment(s)"));
    }

    // ========================================
    // HELPER METHOD TESTS
    // ========================================

    @Test
    void getUserIdFromAuthentication_WithValidUser_ShouldReturnUserId() throws Exception {
        // This test is implicitly covered by other tests that use authentication
        // The method is private but tested through public endpoints
        mockMvc.perform(get("/api/v1/comments/my-comments")
                .principal(authentication))
                .andExpect(status().isOk());
    }

    @Test
    void getUserIdFromAuthenticationOrNull_WithNullAuthentication_ShouldReturnNull() throws Exception {
        // Clear authentication
        SecurityContextHolder.clearContext();
        
        // This should work without authentication
        mockMvc.perform(get("/api/v1/comments/1"))
                .andExpect(status().isOk());
    }
}