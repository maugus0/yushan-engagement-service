package com.yushan.engagement_service.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.AuthenticationException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for JwtAuthenticationEntryPoint
 */
@ExtendWith(MockitoExtension.class)
class JwtAuthenticationEntryPointTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private AuthenticationException authException;

    private JwtAuthenticationEntryPoint entryPoint;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    void setUp() throws IOException {
        entryPoint = new JwtAuthenticationEntryPoint();
        outputStream = new ByteArrayOutputStream();
        when(response.getOutputStream()).thenReturn(new jakarta.servlet.ServletOutputStream() {
            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public void setWriteListener(jakarta.servlet.WriteListener writeListener) {
                // Not needed for testing
            }

            @Override
            public void write(int b) throws IOException {
                outputStream.write(b);
            }
        });
    }

    @Test
    void commence_WithValidRequest_SetsCorrectResponseHeaders() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        verify(response).setContentType("application/json");
        verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    }

    @Test
    void commence_WithValidRequest_ReturnsCorrectResponseBody() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        String responseBody = outputStream.toString();
        assertTrue(responseBody.contains("\"success\":false"));
        assertTrue(responseBody.contains("\"message\":\"Unauthorized: Invalid or missing JWT token\""));
        assertTrue(responseBody.contains("\"error\":\"UNAUTHORIZED\""));
        assertTrue(responseBody.contains("\"status\":401"));
        assertTrue(responseBody.contains("\"path\":\"/api/v1/test\""));
        assertTrue(responseBody.contains("\"timestamp\""));
    }

    @Test
    void commence_WithBadCredentialsException_ReturnsSpecificErrorMessage() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        BadCredentialsException badCredentialsException = new BadCredentialsException("Invalid credentials");

        // Act
        entryPoint.commence(request, response, badCredentialsException);

        // Assert
        String responseBody = outputStream.toString();
        assertTrue(responseBody.contains("\"details\":\"Invalid credentials provided\""));
    }

    @Test
    void commence_WithCredentialsExpiredException_ReturnsSpecificErrorMessage() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        CredentialsExpiredException credentialsExpiredException = new CredentialsExpiredException("Credentials expired");

        // Act
        entryPoint.commence(request, response, credentialsExpiredException);

        // Assert
        String responseBody = outputStream.toString();
        assertTrue(responseBody.contains("\"details\":\"Credentials have expired\""));
    }

    @Test
    void commence_WithNullException_ReturnsGenericMessage() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");

        // Act
        entryPoint.commence(request, response, null);

        // Assert
        String responseBody = outputStream.toString();
        assertTrue(responseBody.contains("\"success\":false"));
        assertTrue(responseBody.contains("\"message\":\"Unauthorized: Invalid or missing JWT token\""));
        assertFalse(responseBody.contains("\"details\""));
    }

    @Test
    void commence_WithUnknownException_ReturnsGenericErrorMessage() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");
        org.springframework.security.authentication.AuthenticationServiceException unknownException = 
            new org.springframework.security.authentication.AuthenticationServiceException("Unknown error");

        // Act
        entryPoint.commence(request, response, unknownException);

        // Assert
        String responseBody = outputStream.toString();
        assertTrue(responseBody.contains("\"details\":\"Authentication failed: Unknown error\""));
    }

    @Test
    void commence_ResponseIsValidJson() throws IOException, ServletException {
        // Arrange
        when(request.getRequestURI()).thenReturn("/api/v1/test");

        // Act
        entryPoint.commence(request, response, authException);

        // Assert
        String responseBody = outputStream.toString();
        ObjectMapper mapper = new ObjectMapper();
        assertDoesNotThrow(() -> {
            @SuppressWarnings("unchecked")
            Map<String, Object> responseMap = mapper.readValue(responseBody, Map.class);
            assertNotNull(responseMap);
        });
    }
}
