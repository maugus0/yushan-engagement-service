package com.yushan.engagement_service.service;

import com.yushan.engagement_service.client.ContentServiceClient;
import com.yushan.engagement_service.client.UserServiceClient;
import com.yushan.engagement_service.dao.CommentMapper;
import com.yushan.engagement_service.dao.ReportMapper;
import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.dto.novel.NovelDetailResponseDTO;
import com.yushan.engagement_service.dto.report.ReportCreateRequestDTO;
import com.yushan.engagement_service.dto.report.ReportResolutionRequestDTO;
import com.yushan.engagement_service.dto.report.ReportResponseDTO;
import com.yushan.engagement_service.dto.report.ReportSearchRequestDTO;
import com.yushan.engagement_service.entity.Comment;
import com.yushan.engagement_service.entity.Report;
import com.yushan.engagement_service.enums.ReportStatus;
import com.yushan.engagement_service.enums.ReportType;
import com.yushan.engagement_service.exception.ResourceNotFoundException;
import com.yushan.engagement_service.exception.ValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ReportServiceTest {

    @Mock
    private ReportMapper reportMapper;

    @Mock
    private ContentServiceClient contentServiceClient;

    @Mock
    private UserServiceClient userServiceClient;

    @Mock
    private CommentMapper commentMapper;

    @InjectMocks
    private ReportService reportService;

    private UUID testReporterId;
    private UUID testAdminId;
    private Integer testNovelId;
    private Integer testCommentId;
    private Report testReport;
    private NovelDetailResponseDTO testNovel;
    private Comment testComment;
    private ReportCreateRequestDTO testCreateRequest;
    private ReportResolutionRequestDTO testResolutionRequest;

    @BeforeEach
    void setUp() {
        testReporterId = UUID.randomUUID();
        testAdminId = UUID.randomUUID();
        testNovelId = 1;
        testCommentId = 1;
        
        testNovel = new NovelDetailResponseDTO();
        testNovel.setId(testNovelId);
        testNovel.setTitle("Test Novel");
        testNovel.setAuthorId(UUID.randomUUID()); // Different from testReporterId

        testComment = new Comment();
        testComment.setId(testCommentId);
        testComment.setUserId(UUID.randomUUID()); // Different from testReporterId
        testComment.setContent("Test comment content");

        testReport = new Report();
        testReport.setId(1);
        testReport.setUuid(UUID.randomUUID());
        testReport.setReporterId(testReporterId);
        testReport.setReportType(ReportType.SPAM.name());
        testReport.setReason("Spam content");
        testReport.setStatus(ReportStatus.IN_REVIEW.name());
        testReport.setContentType("NOVEL");
        testReport.setContentId(testNovelId);
        testReport.setCreatedAt(new Date());
        testReport.setUpdatedAt(new Date());

        testCreateRequest = new ReportCreateRequestDTO();
        testCreateRequest.setReportType("SPAM");
        testCreateRequest.setReason("Spam content");

        testResolutionRequest = new ReportResolutionRequestDTO();
        testResolutionRequest.setAction("RESOLVED");
        testResolutionRequest.setAdminNotes("Report resolved");
    }

    @Test
    void createNovelReport_WithValidData_ShouldCreateReport() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(reportMapper.existsReportByUserAndContent(testReporterId, "NOVEL", testNovelId)).thenReturn(false);
        when(reportMapper.insertSelective(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            report.setId(1);
            return 1;
        });
        when(userServiceClient.getUsernameById(testReporterId)).thenReturn("testuser");

        // Act
        ReportResponseDTO result = reportService.createNovelReport(testReporterId, testNovelId, testCreateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testReporterId, result.getReporterId());
        assertEquals("SPAM", result.getReportType());
        assertEquals("Spam content", result.getReason());
        assertEquals(ReportStatus.IN_REVIEW.name(), result.getStatus());
        assertEquals("NOVEL", result.getContentType());
        assertEquals(testNovelId, result.getContentId());
        assertEquals("testuser", result.getReporterUsername());
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(reportMapper).existsReportByUserAndContent(testReporterId, "NOVEL", testNovelId);
        verify(reportMapper).insertSelective(any(Report.class));
    }

    @Test
    void createNovelReport_WithNonExistentNovel_ShouldThrowException() {
        // Arrange
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportService.createNovelReport(testReporterId, testNovelId, testCreateRequest);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(reportMapper, never()).insertSelective(any(Report.class));
    }

    @Test
    void createNovelReport_WithAuthorReportingOwnNovel_ShouldThrowException() {
        // Arrange
        testNovel.setAuthorId(testReporterId); // Same as testReporterId
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            reportService.createNovelReport(testReporterId, testNovelId, testCreateRequest);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(reportMapper, never()).insertSelective(any(Report.class));
    }

    @Test
    void createNovelReport_WithInvalidReportType_ShouldThrowException() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        testCreateRequest.setReportType("INVALID_TYPE");
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            reportService.createNovelReport(testReporterId, testNovelId, testCreateRequest);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(reportMapper, never()).insertSelective(any(Report.class));
    }

    @Test
    void createNovelReport_WithExistingReport_ShouldThrowException() {
        // Arrange
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);
        when(reportMapper.existsReportByUserAndContent(testReporterId, "NOVEL", testNovelId)).thenReturn(true);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            reportService.createNovelReport(testReporterId, testNovelId, testCreateRequest);
        });
        
        verify(contentServiceClient).getNovelById(testNovelId);
        verify(reportMapper).existsReportByUserAndContent(testReporterId, "NOVEL", testNovelId);
        verify(reportMapper, never()).insertSelective(any(Report.class));
    }

    @Test
    void createCommentReport_WithValidData_ShouldCreateReport() {
        // Arrange
        when(commentMapper.selectByPrimaryKey(testCommentId)).thenReturn(testComment);
        when(reportMapper.existsReportByUserAndContent(testReporterId, "COMMENT", testCommentId)).thenReturn(false);
        when(reportMapper.insertSelective(any(Report.class))).thenAnswer(invocation -> {
            Report report = invocation.getArgument(0);
            report.setId(1);
            return 1;
        });
        when(userServiceClient.getUsernameById(testReporterId)).thenReturn("testuser");

        // Act
        ReportResponseDTO result = reportService.createCommentReport(testReporterId, testCommentId, testCreateRequest);

        // Assert
        assertNotNull(result);
        assertEquals(testReporterId, result.getReporterId());
        assertEquals("SPAM", result.getReportType());
        assertEquals("Spam content", result.getReason());
        assertEquals(ReportStatus.IN_REVIEW.name(), result.getStatus());
        assertEquals("COMMENT", result.getContentType());
        assertEquals(testCommentId, result.getContentId());
        assertEquals("testuser", result.getReporterUsername());
        
        verify(commentMapper).selectByPrimaryKey(testCommentId);
        verify(reportMapper).existsReportByUserAndContent(testReporterId, "COMMENT", testCommentId);
        verify(reportMapper).insertSelective(any(Report.class));
    }

    @Test
    void createCommentReport_WithNonExistentComment_ShouldThrowException() {
        // Arrange
        when(commentMapper.selectByPrimaryKey(testCommentId)).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportService.createCommentReport(testReporterId, testCommentId, testCreateRequest);
        });
        
        verify(commentMapper).selectByPrimaryKey(testCommentId);
        verify(reportMapper, never()).insertSelective(any(Report.class));
    }

    @Test
    void createCommentReport_WithUserReportingOwnComment_ShouldThrowException() {
        // Arrange
        testComment.setUserId(testReporterId); // Same as testReporterId
        when(commentMapper.selectByPrimaryKey(testCommentId)).thenReturn(testComment);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            reportService.createCommentReport(testReporterId, testCommentId, testCreateRequest);
        });
        
        verify(commentMapper).selectByPrimaryKey(testCommentId);
        verify(reportMapper, never()).insertSelective(any(Report.class));
    }

    @Test
    void getReportsForAdmin_WithValidData_ShouldReturnReports() {
        // Arrange
        ReportSearchRequestDTO request = new ReportSearchRequestDTO();
        request.setPage(0);
        request.setSize(10);
        
        when(reportMapper.selectReportsWithPagination(request)).thenReturn(Arrays.asList(testReport));
        when(reportMapper.countReports(request)).thenReturn(1L);
        when(userServiceClient.getUsernameById(testReporterId)).thenReturn("testuser");
        
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act
        PageResponseDTO<ReportResponseDTO> result = reportService.getReportsForAdmin(request);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getTotalElements());
        assertEquals(0, result.getCurrentPage());
        assertEquals(10, result.getSize());
        
        verify(reportMapper).selectReportsWithPagination(request);
        verify(reportMapper).countReports(request);
    }

    @Test
    void getReportById_WithValidId_ShouldReturnReport() {
        // Arrange
        when(reportMapper.selectByPrimaryKey(1)).thenReturn(testReport);
        when(userServiceClient.getUsernameById(testReporterId)).thenReturn("testuser");
        
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act
        ReportResponseDTO result = reportService.getReportById(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals(testReporterId, result.getReporterId());
        assertEquals("testuser", result.getReporterUsername());
        
        verify(reportMapper).selectByPrimaryKey(1);
    }

    @Test
    void getReportById_WithNonExistentId_ShouldThrowException() {
        // Arrange
        when(reportMapper.selectByPrimaryKey(1)).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportService.getReportById(1);
        });
        
        verify(reportMapper).selectByPrimaryKey(1);
    }

    @Test
    void resolveReport_WithValidData_ShouldResolveReport() {
        // Arrange
        when(reportMapper.selectByPrimaryKey(1)).thenReturn(testReport);
        when(reportMapper.updateReportStatus(1, "RESOLVED", "Report resolved", testAdminId)).thenReturn(1);
        when(userServiceClient.getUsernameById(testReporterId)).thenReturn("testuser");
        when(userServiceClient.getUsernameById(testAdminId)).thenReturn("admin");
        
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act
        ReportResponseDTO result = reportService.resolveReport(1, testAdminId, testResolutionRequest);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        
        verify(reportMapper, times(2)).selectByPrimaryKey(1);
        verify(reportMapper).updateReportStatus(1, "RESOLVED", "Report resolved", testAdminId);
    }

    @Test
    void resolveReport_WithNonExistentReport_ShouldThrowException() {
        // Arrange
        when(reportMapper.selectByPrimaryKey(1)).thenReturn(null);

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> {
            reportService.resolveReport(1, testAdminId, testResolutionRequest);
        });
        
        verify(reportMapper).selectByPrimaryKey(1);
        verify(reportMapper, never()).updateReportStatus(anyInt(), anyString(), anyString(), any());
    }

    @Test
    void resolveReport_WithInvalidAction_ShouldThrowException() {
        // Arrange
        testResolutionRequest.setAction("INVALID_ACTION");
        when(reportMapper.selectByPrimaryKey(1)).thenReturn(testReport);

        // Act & Assert
        assertThrows(ValidationException.class, () -> {
            reportService.resolveReport(1, testAdminId, testResolutionRequest);
        });
        
        verify(reportMapper).selectByPrimaryKey(1);
        verify(reportMapper, never()).updateReportStatus(anyInt(), anyString(), anyString(), any());
    }

    @Test
    void getReportsByReporter_WithValidData_ShouldReturnReports() {
        // Arrange
        when(reportMapper.selectReportsByReporterId(testReporterId)).thenReturn(Arrays.asList(testReport));
        when(userServiceClient.getUsernameById(testReporterId)).thenReturn("testuser");
        
        ApiResponse<NovelDetailResponseDTO> novelResponse = new ApiResponse<>();
        novelResponse.setData(testNovel);
        when(contentServiceClient.getNovelById(testNovelId)).thenReturn(novelResponse);

        // Act
        List<ReportResponseDTO> result = reportService.getReportsByReporter(testReporterId);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(testReporterId, result.get(0).getReporterId());
        
        verify(reportMapper).selectReportsByReporterId(testReporterId);
    }

    @Test
    void getReportsByReporter_WithNoReports_ShouldReturnEmptyList() {
        // Arrange
        when(reportMapper.selectReportsByReporterId(testReporterId)).thenReturn(Collections.emptyList());

        // Act
        List<ReportResponseDTO> result = reportService.getReportsByReporter(testReporterId);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        
        verify(reportMapper).selectReportsByReporterId(testReporterId);
    }
}