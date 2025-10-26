package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.report.ReportCreateRequestDTO;
import com.yushan.engagement_service.dto.report.ReportResolutionRequestDTO;
import com.yushan.engagement_service.dto.report.ReportResponseDTO;
import com.yushan.engagement_service.dto.report.ReportSearchRequestDTO;
import com.yushan.engagement_service.service.ReportService;
import com.yushan.engagement_service.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/reports")
@Tag(name = "Report Management", description = "APIs for managing content reports")
public class ReportController {

    @Autowired
    private ReportService reportService;

    /**
     * Report a novel
     */
    @PostMapping("/novel/{novelId}")
    @Operation(summary = "[USER] Report novel", description = "Report a novel for inappropriate content. One report per novel per user.")
    public ApiResponse<ReportResponseDTO> reportNovel(
            @PathVariable Integer novelId,
            @Valid @RequestBody ReportCreateRequestDTO request,
            Authentication authentication) {
        
        UUID reporterId = extractUserId(authentication);
        ReportResponseDTO report = reportService.createNovelReport(reporterId, novelId, request);
        return ApiResponse.success("Novel reported successfully", report);
    }

    /**
     * Report a comment
     */
    @PostMapping("/comment/{commentId}")
    @Operation(summary = "[USER] Report comment", description = "Report a comment for inappropriate content. One report per comment per user.")
    public ApiResponse<ReportResponseDTO> reportComment(
            @PathVariable Integer commentId,
            @Valid @RequestBody ReportCreateRequestDTO request,
            Authentication authentication) {
        
        UUID reporterId = extractUserId(authentication);
        ReportResponseDTO report = reportService.createCommentReport(reporterId, commentId, request);
        return ApiResponse.success("Comment reported successfully", report);
    }

    /**
     * Get user's own reports
     */
    @GetMapping("/my-reports")
    @Operation(summary = "[USER] Get my reports", description = "Get all reports submitted by the current user.")
    public ApiResponse<List<ReportResponseDTO>> getMyReports(Authentication authentication) {
        UUID reporterId = extractUserId(authentication);
        List<ReportResponseDTO> reports = reportService.getReportsByReporter(reporterId);
        return ApiResponse.success("Reports retrieved successfully", reports);
    }

    /**
     * Get all reports for admin dashboard
     */
    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Get all reports", description = "Get all reports with pagination and filtering for admin dashboard.")
    public ApiResponse<PageResponseDTO<ReportResponseDTO>> getReportsForAdmin(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String reportType,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "createdAt") String sort,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        ReportSearchRequestDTO request = new ReportSearchRequestDTO();
        request.setStatus(status);
        request.setReportType(reportType);
        request.setSearch(search);
        request.setSort(sort);
        request.setOrder(order);
        request.setPage(page);
        request.setSize(size);

        PageResponseDTO<ReportResponseDTO> reports = reportService.getReportsForAdmin(request);
        return ApiResponse.success("Reports retrieved successfully", reports);
    }

    /**
     * Get report details by ID (admin only)
     */
    @GetMapping("/admin/{reportId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Get report details", description = "Get detailed information about a specific report.")
    public ApiResponse<ReportResponseDTO> getReportDetails(@PathVariable Integer reportId) {
        ReportResponseDTO report = reportService.getReportById(reportId);
        return ApiResponse.success("Report details retrieved successfully", report);
    }

    /**
     * Resolve a report (admin only)
     */
    @PutMapping("/admin/{reportId}/resolve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "[ADMIN] Resolve report", description = "Resolve a report by marking it as RESOLVED or DISMISSED with admin notes.")
    public ApiResponse<ReportResponseDTO> resolveReport(
            @PathVariable Integer reportId,
            @Valid @RequestBody ReportResolutionRequestDTO request,
            Authentication authentication) {
        
        UUID adminId = extractUserId(authentication);
        ReportResponseDTO report = reportService.resolveReport(reportId, adminId, request);
        return ApiResponse.success("Report resolved successfully", report);
    }

    /**
     * Extract user ID from authentication
     */
    private UUID extractUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new RuntimeException("Authentication required");
        }

        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) principal;
            return UUID.fromString(userDetails.getUserId());
        }

        throw new RuntimeException("Invalid authentication");
    }
}
