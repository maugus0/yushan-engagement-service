package com.yushan.engagement_service;

import com.yushan.engagement_service.service.ReviewService;
import com.yushan.engagement_service.service.CommentService;
import com.yushan.engagement_service.service.VoteService;
import com.yushan.engagement_service.service.ReportService;
import com.yushan.engagement_service.service.KafkaEventProducerService;
import com.yushan.engagement_service.dao.ReviewMapper;
import com.yushan.engagement_service.dao.CommentMapper;
import com.yushan.engagement_service.dao.VoteMapper;
import com.yushan.engagement_service.dao.ReportMapper;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.ActiveProfiles;

/**
 * Basic integration test to verify that the application context loads successfully.
 */
@SpringBootTest
@ActiveProfiles("test")
class EngagementServiceApplicationTests {

    @MockitoBean
    private ReviewService reviewService;

    @MockitoBean
    private CommentService commentService;

    @MockitoBean
    private VoteService voteService;

    @MockitoBean
    private ReportService reportService;

    @MockitoBean
    private ReviewMapper reviewMapper;

    @MockitoBean
    private CommentMapper commentMapper;

    @MockitoBean
    private VoteMapper voteMapper;

    @MockitoBean
    private ReportMapper reportMapper;

    @MockitoBean
    private KafkaEventProducerService kafkaEventProducerService;

    @Test
    void contextLoads() {
        // This test will pass if the application context loads successfully
    }

    @Test
    void mainMethodCanBeCalled() {
        // Test that the main method can be called without errors
        // This is a basic smoke test for the application entry point
        try (MockedStatic<SpringApplication> mockedSpringApplication = 
             Mockito.mockStatic(SpringApplication.class)) {
            
            // Mock SpringApplication.run to return null (no-op)
            mockedSpringApplication.when(() -> 
                SpringApplication.run(EngagementServiceApplication.class, new String[]{}))
                .thenReturn(null);
            
            // Test main method - this should now execute successfully
            EngagementServiceApplication.main(new String[]{});
            
            // Verify SpringApplication.run was called with correct parameters
            mockedSpringApplication.verify(() -> 
                SpringApplication.run(EngagementServiceApplication.class, new String[]{}));
        }
    }

    @Test
    void mainMethodWithMockedSpringApplication() {
        // Test main method with mocked SpringApplication to achieve 100% coverage
        try (MockedStatic<SpringApplication> mockedSpringApplication = 
             Mockito.mockStatic(SpringApplication.class)) {
            
            // Mock SpringApplication.run to return null (no-op)
            mockedSpringApplication.when(() -> 
                SpringApplication.run(EngagementServiceApplication.class, new String[]{}))
                .thenReturn(null);
            
            // Test main method - this should now execute successfully
            EngagementServiceApplication.main(new String[]{});
            
            // Verify SpringApplication.run was called with correct parameters
            mockedSpringApplication.verify(() -> 
                SpringApplication.run(EngagementServiceApplication.class, new String[]{}));
        }
    }

    @Test
    void mainMethodWithArgs() {
        // Test main method with command line arguments using mocked SpringApplication
        try (MockedStatic<SpringApplication> mockedSpringApplication = 
             Mockito.mockStatic(SpringApplication.class)) {
            
            String[] testArgs = {"--spring.profiles.active=test", "--server.port=8080"};
            
            mockedSpringApplication.when(() -> 
                SpringApplication.run(EngagementServiceApplication.class, testArgs))
                .thenReturn(null);
            
            EngagementServiceApplication.main(testArgs);
            
            mockedSpringApplication.verify(() -> 
                SpringApplication.run(EngagementServiceApplication.class, testArgs));
        }
    }
}
