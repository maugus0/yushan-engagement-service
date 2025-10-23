package com.yushan.engagement_service.security;

import com.yushan.engagement_service.dto.event.UserActivityEvent;
import com.yushan.engagement_service.service.KafkaEventProducerService;
import com.yushan.engagement_service.util.SecurityUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.UUID;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserActivityFilterTest {

    @Mock
    private KafkaEventProducerService kafkaEventProducerService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private UserActivityFilter filter;

    @BeforeEach
    void setUp() {
        // Clear any static state
    }

    @Test
    void doFilterInternal_WithAuthenticatedRequest_ShouldPublishUserActivityEvent() throws ServletException, IOException {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        when(request.getMethod()).thenReturn("GET");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(kafkaEventProducerService).publishUserActivityEvent(argThat(event -> 
                event.userId().equals(userId) &&
                event.serviceName().equals("engagement-service") &&
                event.endpoint().equals("/api/v1/test") &&
                event.method().equals("GET") &&
                event.timestamp() != null
            ));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_WithNonAuthenticatedRequest_ShouldNotPublishEvent() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(kafkaEventProducerService, never()).publishUserActivityEvent(any(UserActivityEvent.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidAuthHeader_ShouldNotPublishEvent() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(kafkaEventProducerService, never()).publishUserActivityEvent(any(UserActivityEvent.class));
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNullUserId_ShouldNotPublishEvent() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(null);

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(kafkaEventProducerService, never()).publishUserActivityEvent(any(UserActivityEvent.class));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_WithKafkaServiceException_ShouldContinueFilterChain() throws ServletException, IOException {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        when(request.getMethod()).thenReturn("GET");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(userId);
            doThrow(new RuntimeException("Kafka error")).when(kafkaEventProducerService)
                .publishUserActivityEvent(any(UserActivityEvent.class));

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_WithSecurityUtilsException_ShouldContinueFilterChain() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenThrow(new RuntimeException("Security error"));

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(kafkaEventProducerService, never()).publishUserActivityEvent(any(UserActivityEvent.class));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_WithPostRequest_ShouldPublishCorrectEvent() throws ServletException, IOException {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(request.getRequestURI()).thenReturn("/api/v1/comments");
        when(request.getMethod()).thenReturn("POST");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(kafkaEventProducerService).publishUserActivityEvent(argThat(event -> 
                event.userId().equals(userId) &&
                event.serviceName().equals("engagement-service") &&
                event.endpoint().equals("/api/v1/comments") &&
                event.method().equals("POST")
            ));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_WithPutRequest_ShouldPublishCorrectEvent() throws ServletException, IOException {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(request.getRequestURI()).thenReturn("/api/v1/reviews/123");
        when(request.getMethod()).thenReturn("PUT");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(kafkaEventProducerService).publishUserActivityEvent(argThat(event -> 
                event.userId().equals(userId) &&
                event.serviceName().equals("engagement-service") &&
                event.endpoint().equals("/api/v1/reviews/123") &&
                event.method().equals("PUT")
            ));
            verify(filterChain).doFilter(request, response);
        }
    }

    @Test
    void doFilterInternal_WithDeleteRequest_ShouldPublishCorrectEvent() throws ServletException, IOException {
        // Arrange
        UUID userId = UUID.randomUUID();
        when(request.getHeader("Authorization")).thenReturn("Bearer valid-token");
        when(request.getRequestURI()).thenReturn("/api/v1/comments/456");
        when(request.getMethod()).thenReturn("DELETE");

        try (MockedStatic<SecurityUtils> securityUtilsMock = mockStatic(SecurityUtils.class)) {
            securityUtilsMock.when(SecurityUtils::getCurrentUserId).thenReturn(userId);

            // Act
            filter.doFilterInternal(request, response, filterChain);

            // Assert
            verify(kafkaEventProducerService).publishUserActivityEvent(argThat(event -> 
                event.userId().equals(userId) &&
                event.serviceName().equals("engagement-service") &&
                event.endpoint().equals("/api/v1/comments/456") &&
                event.method().equals("DELETE")
            ));
            verify(filterChain).doFilter(request, response);
        }
    }

}
