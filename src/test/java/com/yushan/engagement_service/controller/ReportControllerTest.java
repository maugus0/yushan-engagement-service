package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.report.ReportCreateRequestDTO;
import com.yushan.engagement_service.dto.report.ReportResolutionRequestDTO;
import com.yushan.engagement_service.dto.report.ReportResponseDTO;
import com.yushan.engagement_service.dto.report.ReportSearchRequestDTO;
import com.yushan.engagement_service.security.CustomUserDetails;
import com.yushan.engagement_service.service.ReportService;
import com.yushan.engagement_service.util.JwtTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @MockBean
    private JwtTestUtil jwtTestUtil;

    private CustomUserDetails userDetails;
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        userDetails = new CustomUserDetails(
                "550e8400-e29b-41d4-a716-446655440001",
                "test@example.com",
                "testuser",
                "ADMIN",
                0
        );
        authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Test
    void reportNovel_ShouldCreateNovelReport() throws Exception {
        // Arrange
        Integer novelId = 1;
        ReportCreateRequestDTO request = new ReportCreateRequestDTO();
        request.setReportType("INAPPROPRIATE");
        request.setReason("Inappropriate content");

        ReportResponseDTO response = new ReportResponseDTO();
        response.setId(1);
        response.setNovelId(novelId);
        response.setReporterId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));

        when(reportService.createNovelReport(any(UUID.class), eq(novelId), any(ReportCreateRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/reports/novel/{novelId}", novelId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reportType\":\"INAPPROPRIATE\",\"reason\":\"Inappropriate content\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Novel reported successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.novelId").value(novelId));
    }

    @Test
    void reportComment_ShouldCreateCommentReport() throws Exception {
        // Arrange
        Integer commentId = 1;
        ReportCreateRequestDTO request = new ReportCreateRequestDTO();
        request.setReportType("SPAM");
        request.setReason("Spam comment");

        ReportResponseDTO response = new ReportResponseDTO();
        response.setId(1);
        response.setCommentId(commentId);
        response.setReporterId(UUID.fromString("550e8400-e29b-41d4-a716-446655440001"));

        when(reportService.createCommentReport(any(UUID.class), eq(commentId), any(ReportCreateRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(post("/api/v1/reports/comment/{commentId}", commentId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reportType\":\"SPAM\",\"reason\":\"Spam comment\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Comment reported successfully"))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.commentId").value(commentId));
    }

    @Test
    void getMyReports_ShouldReturnUserReports() throws Exception {
        // Arrange
        ReportResponseDTO report1 = new ReportResponseDTO();
        report1.setId(1);
        report1.setNovelId(1);

        ReportResponseDTO report2 = new ReportResponseDTO();
        report2.setId(2);
        report2.setCommentId(1);

        List<ReportResponseDTO> reports = Arrays.asList(report1, report2);

        when(reportService.getReportsByReporter(any(UUID.class))).thenReturn(reports);

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/my-reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Reports retrieved successfully"))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data.length()").value(2));
    }

    @Test
    void getReportsForAdmin_ShouldReturnPaginatedReports() throws Exception {
        // Arrange
        ReportResponseDTO report = new ReportResponseDTO();
        report.setId(1);
        report.setNovelId(1);

        PageResponseDTO<ReportResponseDTO> pageResponse = new PageResponseDTO<>();
        pageResponse.setContent(Arrays.asList(report));
        pageResponse.setTotalElements(1);
        pageResponse.setTotalPages(1);
        pageResponse.setCurrentPage(0);
        pageResponse.setSize(10);

        when(reportService.getReportsForAdmin(any(ReportSearchRequestDTO.class))).thenReturn(pageResponse);

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/admin")
                .param("status", "IN_REVIEW")
                .param("reportType", "INAPPROPRIATE")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Reports retrieved successfully"))
                .andExpect(jsonPath("$.data.content").isArray())
                .andExpect(jsonPath("$.data.totalElements").value(1));
    }

    @Test
    void getReportDetails_ShouldReturnReportDetails() throws Exception {
        // Arrange
        Integer reportId = 1;
        ReportResponseDTO report = new ReportResponseDTO();
        report.setId(reportId);
        report.setNovelId(1);

        when(reportService.getReportById(reportId)).thenReturn(report);

        // Act & Assert
        mockMvc.perform(get("/api/v1/reports/admin/{reportId}", reportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Report details retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(reportId));
    }

    @Test
    void resolveReport_ShouldResolveReport() throws Exception {
        // Arrange
        Integer reportId = 1;
        ReportResolutionRequestDTO request = new ReportResolutionRequestDTO();
        request.setAction("RESOLVED");
        request.setAdminNotes("Report resolved");

        ReportResponseDTO response = new ReportResponseDTO();
        response.setId(reportId);
        response.setStatus("RESOLVED");

        when(reportService.resolveReport(eq(reportId), any(UUID.class), any(ReportResolutionRequestDTO.class)))
                .thenReturn(response);

        // Act & Assert
        mockMvc.perform(put("/api/v1/reports/admin/{reportId}/resolve", reportId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"action\":\"RESOLVED\",\"adminNotes\":\"Report resolved\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Report resolved successfully"))
                .andExpect(jsonPath("$.data.id").value(reportId))
                .andExpect(jsonPath("$.data.status").value("RESOLVED"));
    }

    @Test
    void reportNovel_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/reports/novel/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reportType\":\"\",\"description\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reportComment_WithInvalidData_ShouldReturnBadRequest() throws Exception {
        // Act & Assert
        mockMvc.perform(post("/api/v1/reports/comment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"reportType\":\"\",\"description\":\"\"}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getReportsForAdmin_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        ReportResponseDTO mockReport = new ReportResponseDTO();
        mockReport.setId(1);
        mockReport.setContentType("NOVEL");
        mockReport.setStatus("PENDING");
        
        List<ReportResponseDTO> mockReports = Arrays.asList(mockReport);
        
        PageResponseDTO<ReportResponseDTO> mockPageResponse = new PageResponseDTO<>();
        mockPageResponse.setContent(mockReports);
        mockPageResponse.setCurrentPage(0);
        mockPageResponse.setSize(10);
        mockPageResponse.setTotalElements(1L);
        mockPageResponse.setTotalPages(1);
        
        when(reportService.getReportsForAdmin(any(ReportSearchRequestDTO.class))).thenReturn(mockPageResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/reports/admin")
                .param("status", "PENDING")
                .param("reportType", "SPAM")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Reports retrieved successfully"))
                .andExpect(jsonPath("$.data.content[0].id").value(1))
                .andExpect(jsonPath("$.data.content[0].contentType").value("NOVEL"));
    }

    @Test
    void getReportDetails_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        Integer testReportId = 1;
        
        ReportResponseDTO mockResponse = new ReportResponseDTO();
        mockResponse.setId(testReportId);
        mockResponse.setContentType("NOVEL");
        mockResponse.setStatus("PENDING");
        
        when(reportService.getReportById(eq(testReportId))).thenReturn(mockResponse);

        // Execute & Verify
        mockMvc.perform(get("/api/v1/reports/admin/{reportId}", testReportId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Report details retrieved successfully"))
                .andExpect(jsonPath("$.data.id").value(testReportId))
                .andExpect(jsonPath("$.data.contentType").value("NOVEL"));
    }
}
