package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.dto.comment.*;
import com.yushan.engagement_service.dto.common.*;
import com.yushan.engagement_service.enums.ErrorCode;
import com.yushan.engagement_service.service.CommentService;
import com.yushan.engagement_service.security.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/comments")
@Tag(name = "Comment Management", description = "APIs for managing comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    // ========================================
    // PUBLIC & USER ENDPOINTS
    // ========================================

    /**
     * Create a new comment (authenticated users only)
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "[USER] Create comment", description = "Create a new comment on a chapter. One per chapter per user.")
    public ApiResponse<CommentResponseDTO> createComment(@Valid @RequestBody CommentCreateRequestDTO request,
                                                         Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CommentResponseDTO dto = commentService.createComment(userId, request);
        return ApiResponse.success("Comment created successfully", dto);
    }

    /**
     * Update an existing comment (only the author)
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @Operation(summary = "[USER] Update comment", description = "Update an existing comment. Only author can update.")
    public ApiResponse<CommentResponseDTO> updateComment(@PathVariable Integer id,
                                                         @Valid @RequestBody CommentUpdateRequestDTO request,
                                                         Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CommentResponseDTO dto = commentService.updateComment(id, userId, request);
        return ApiResponse.success("Comment updated successfully", dto);
    }

    /**
     * Delete a comment (only the author or admin)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @Operation(summary = "[USER/ADMIN] Delete comment", description = "Delete a comment. Author or admin only.")
    public ApiResponse<String> deleteComment(@PathVariable Integer id,
                                             Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));

        boolean deleted = commentService.deleteComment(id, userId, isAdmin);
        if (deleted) {
            return ApiResponse.success("Comment deleted successfully");
        }
        return ApiResponse.error(ErrorCode.BAD_REQUEST, "Failed to delete comment");
    }

    /**
     * Get comment by ID (public)
     */
    @GetMapping("/{id}")
    @Operation(summary = "[PUBLIC] Get comment by id", description = "Retrieve a single comment by its ID.")
    public ApiResponse<CommentResponseDTO> getComment(@PathVariable Integer id,
                                                      Authentication authentication) {
        UUID userId = getUserIdFromAuthenticationOrNull(authentication);
        CommentResponseDTO dto = commentService.getComment(id, userId);
        return ApiResponse.success("Comment retrieved successfully", dto);
    }

    /**
     * Get comments for a specific chapter (public)
     */
    @GetMapping("/chapter/{chapterId}")
    @Operation(summary = "[PUBLIC] Get comments by chapter", description = "List comments for a chapter with pagination and sorting.")
    public ApiResponse<CommentListResponseDTO> getCommentsByChapter(
            @PathVariable Integer chapterId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "createTime") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            Authentication authentication) {

        UUID userId = getUserIdFromAuthenticationOrNull(authentication);
        CommentListResponseDTO response = commentService.getCommentsByChapter(chapterId, userId, page, size, sort, order);
        return ApiResponse.success("Comments retrieved successfully", response);
    }

    /**
     * Get comments for a specific novel (public, across all chapters)
     */
    @GetMapping("/novel/{novelId}")
    @Operation(summary = "[PUBLIC] Get comments by novel", description = "List comments across all chapters of a novel with filters.")
    public ApiResponse<CommentListResponseDTO> getCommentsByNovel(
            @PathVariable Integer novelId,
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "createTime") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "isSpoiler", required = false) Boolean isSpoiler,
            @RequestParam(value = "search", required = false) String search,
            Authentication authentication) {

        UUID userId = getUserIdFromAuthenticationOrNull(authentication);

        CommentSearchRequestDTO request = CommentSearchRequestDTO.builder()
                .novelId(novelId)
                .isSpoiler(isSpoiler)
                .search(search)
                .sort(sort)
                .order(order)
                .page(page)
                .size(size)
                .build();

        CommentListResponseDTO response = commentService.getCommentsByNovel(novelId, userId, request);
        return ApiResponse.success("Comments retrieved successfully", response);
    }

    /**
     * Like a comment (authenticated users only)
     */
    @PostMapping("/{id}/like")
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @Operation(summary = "[USER] Like comment", description = "Like a comment.")
    public ApiResponse<CommentResponseDTO> likeComment(@PathVariable Integer id,
                                                       Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CommentResponseDTO dto = commentService.toggleLike(id, userId, true);
        return ApiResponse.success("Comment liked successfully", dto);
    }

    /**
     * Unlike a comment (authenticated users only)
     */
    @PostMapping("/{id}/unlike")
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @Operation(summary = "[USER] Unlike comment", description = "Remove like from a comment.")
    public ApiResponse<CommentResponseDTO> unlikeComment(@PathVariable Integer id,
                                                         Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        CommentResponseDTO dto = commentService.toggleLike(id, userId, false);
        return ApiResponse.success("Comment unliked successfully", dto);
    }

    /**
     * Get current user's comments (authenticated users only)
     */
    @GetMapping("/my-comments")
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @Operation(summary = "[USER] Get my comments", description = "List current user's comments.")
    public ApiResponse<List<CommentResponseDTO>> getMyComments(Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        List<CommentResponseDTO> comments = commentService.getUserComments(userId);
        return ApiResponse.success("Your comments retrieved successfully", comments);
    }

    /**
     * Check if user has commented on a chapter (authenticated users only)
     */
    @GetMapping("/check/chapter/{chapterId}")
    @PreAuthorize("hasAnyRole('USER','AUTHOR','ADMIN')")
    @Operation(summary = "[USER] Check commented on chapter", description = "Check if current user has commented on a chapter.")
    public ApiResponse<Boolean> hasUserCommentedOnChapter(@PathVariable Integer chapterId,
                                                          Authentication authentication) {
        UUID userId = getUserIdFromAuthentication(authentication);
        boolean hasCommented = commentService.hasUserCommentedOnChapter(userId, chapterId);
        return ApiResponse.success("Comment status checked", hasCommented);
    }

    /**
     * Get comment statistics for a chapter (public)
     */
    @GetMapping("/chapter/{chapterId}/statistics")
    @Operation(summary = "[PUBLIC] Get chapter comment stats", description = "Retrieve statistics for a chapter's comments.")
    public ApiResponse<CommentStatisticsDTO> getChapterCommentStats(@PathVariable Integer chapterId) {
        CommentStatisticsDTO stats = commentService.getChapterCommentStats(chapterId);
        return ApiResponse.success("Comment statistics retrieved", stats);
    }

    // ========================================
    // ADMIN MODERATION ENDPOINTS
    // ========================================

    /**
     * Get all comments with filtering and pagination (admin only)
     * Primary endpoint for comment moderation dashboard
     */
    @GetMapping("/admin/moderation")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Moderation list", description = "List comments for moderation with filters.")
    public ApiResponse<CommentListResponseDTO> getAllCommentsForModeration(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "50") Integer size,
            @RequestParam(value = "sort", defaultValue = "createTime") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "chapterId", required = false) Integer chapterId,
            @RequestParam(value = "novelId", required = false) Integer novelId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "isSpoiler", required = false) Boolean isSpoiler,
            @RequestParam(value = "search", required = false) String search,
            Authentication authentication) {

        UUID currentUserId = getUserIdFromAuthentication(authentication);

        CommentSearchRequestDTO request = CommentSearchRequestDTO.builder()
                .chapterId(chapterId)
                .novelId(novelId)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .isSpoiler(isSpoiler)
                .search(search)
                .sort(sort)
                .order(order)
                .page(page)
                .size(size)
                .build();

        CommentListResponseDTO response = commentService.getAllComments(request, currentUserId);
        return ApiResponse.success("All comments retrieved successfully", response);
    }

    /**
     * Get all comments (admin search/filter)
     */
    @GetMapping("/admin/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] List all comments", description = "Admin list with pagination and filters.")
    public ApiResponse<CommentListResponseDTO> getAllCommentsAdmin(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "createTime") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "chapterId", required = false) Integer chapterId,
            @RequestParam(value = "novelId", required = false) Integer novelId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "isSpoiler", required = false) Boolean isSpoiler,
            @RequestParam(value = "search", required = false) String search,
            Authentication authentication) {

        UUID currentUserId = getUserIdFromAuthentication(authentication);

        CommentSearchRequestDTO request = CommentSearchRequestDTO.builder()
                .chapterId(chapterId)
                .novelId(novelId)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .isSpoiler(isSpoiler)
                .search(search)
                .sort(sort)
                .order(order)
                .page(page)
                .size(size)
                .build();

        CommentListResponseDTO response = commentService.getAllComments(request, currentUserId);
        return ApiResponse.success("All comments retrieved successfully", response);
    }

    /**
     * Search comments with advanced filters (admin)
     */
    @GetMapping("/admin/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Search comments", description = "Advanced search for comments.")
    public ApiResponse<CommentListResponseDTO> searchCommentsAdmin(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "size", defaultValue = "20") Integer size,
            @RequestParam(value = "sort", defaultValue = "createTime") String sort,
            @RequestParam(value = "order", defaultValue = "desc") String order,
            @RequestParam(value = "chapterId", required = false) Integer chapterId,
            @RequestParam(value = "novelId", required = false) Integer novelId,
            @RequestParam(value = "userId", required = false) String userId,
            @RequestParam(value = "isSpoiler", required = false) Boolean isSpoiler,
            @RequestParam(value = "search", required = false) String search,
            Authentication authentication) {

        UUID currentUserId = getUserIdFromAuthentication(authentication);

        CommentSearchRequestDTO request = CommentSearchRequestDTO.builder()
                .chapterId(chapterId)
                .novelId(novelId)
                .userId(userId != null ? UUID.fromString(userId) : null)
                .isSpoiler(isSpoiler)
                .search(search)
                .sort(sort)
                .order(order)
                .page(page)
                .size(size)
                .build();

        CommentListResponseDTO response = commentService.getAllComments(request, currentUserId);
        return ApiResponse.success("Comments search completed", response);
    }

    /**
     * Get comments by specific user (admin moderation tool)
     */
    @GetMapping("/admin/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Get user's comments", description = "List comments by specific user.")
    public ApiResponse<List<CommentResponseDTO>> getUserCommentsAdmin(
            @PathVariable String userId,
            Authentication authentication) {
        UUID targetUserId = UUID.fromString(userId);
        List<CommentResponseDTO> comments = commentService.getUserComments(targetUserId);
        return ApiResponse.success("User comments retrieved successfully", comments);
    }

    /**
     * Get moderation statistics (admin dashboard)
     */
    @GetMapping("/admin/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Moderation statistics", description = "Retrieve moderation dashboard statistics.")
    public ApiResponse<CommentModerationStatsDTO> getModerationStats() {
        CommentModerationStatsDTO stats = commentService.getModerationStatistics();
        return ApiResponse.success("Moderation statistics retrieved", stats);
    }

    /**
     * Delete any comment (admin only)
     */
    @DeleteMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Delete any comment", description = "Admin can delete any comment by ID.")
    public ApiResponse<String> deleteCommentAdmin(@PathVariable Integer id) {
        boolean deleted = commentService.deleteComment(id, null, true);
        if (deleted) {
            return ApiResponse.success("Comment deleted successfully by admin");
        }
        return ApiResponse.error(ErrorCode.BAD_REQUEST, "Failed to delete comment");
    }

    /**
     * Batch delete comments (admin only)
     */
    @PostMapping("/admin/batch-delete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Batch delete comments", description = "Delete multiple comments by IDs.")
    public ApiResponse<String> batchDeleteComments(@Valid @RequestBody CommentBatchDeleteRequestDTO request) {
        int deletedCount = commentService.batchDeleteComments(request, true);
        return ApiResponse.success("Successfully deleted " + deletedCount + " comment(s)");
    }

    /**
     * Delete all comments by a user (admin moderation action)
     */
    @DeleteMapping("/admin/user/{userId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Delete all user's comments", description = "Delete all comments of a user.")
    public ApiResponse<String> deleteAllUserComments(@PathVariable String userId) {
        UUID targetUserId = UUID.fromString(userId);
        int deletedCount = commentService.deleteAllUserComments(targetUserId);
        return ApiResponse.success("Successfully deleted " + deletedCount + " comment(s) from user");
    }

    /**
     * Delete all comments for a chapter (admin cleanup tool)
     */
    @DeleteMapping("/admin/chapter/{chapterId}/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Delete all chapter comments", description = "Delete all comments in a chapter.")
    public ApiResponse<String> deleteAllChapterComments(@PathVariable Integer chapterId) {
        int deletedCount = commentService.deleteAllChapterComments(chapterId);
        return ApiResponse.success("Successfully deleted " + deletedCount + " comment(s) from chapter");
    }

    /**
     * Bulk update spoiler status (admin moderation tool)
     */
    @PatchMapping("/admin/bulk-spoiler")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Bulk update spoiler", description = "Bulk update spoiler flag for comments.")
    public ApiResponse<String> bulkUpdateSpoilerStatus(
            @Valid @RequestBody CommentBulkSpoilerUpdateRequestDTO request) {
        int updatedCount = commentService.bulkUpdateSpoilerStatus(request);
        return ApiResponse.success("Successfully updated " + updatedCount + " comment(s)");
    }

    // ========================================
    // HELPER METHODS
    // ========================================

    /**
     * Helper method to extract user ID from authentication
     */
    private UUID getUserIdFromAuthentication(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            throw new IllegalArgumentException("Authentication required");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails cud = (CustomUserDetails) principal;
            if (cud.getUserId() != null) {
                return UUID.fromString(cud.getUserId());
            }
        }

        throw new IllegalArgumentException("User ID not found in authentication");
    }

    /**
     * Helper method to extract user ID from authentication (returns null if not authenticated)
     */
    private UUID getUserIdFromAuthenticationOrNull(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return null;
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails cud = (CustomUserDetails) principal;
            if (cud.getUserId() != null) {
                return UUID.fromString(cud.getUserId());
            }
        }

        return null;
    }
}
