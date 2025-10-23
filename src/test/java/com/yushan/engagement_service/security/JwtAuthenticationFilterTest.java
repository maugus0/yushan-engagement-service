package com.yushan.engagement_service.security;

import com.yushan.engagement_service.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationFilterTest {

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_WithValidToken_ShouldSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("user123");
        when(jwtUtil.extractEmail(token)).thenReturn("user@example.com");
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(jwtUtil.extractRole(token)).thenReturn("USER");
        when(jwtUtil.extractStatus(token)).thenReturn(0);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNoAuthorizationHeader_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidTokenFormat_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        when(request.getHeader("Authorization")).thenReturn("InvalidFormat token");

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithInvalidToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "invalid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(false);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNonAccessToken_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "refresh-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(false);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithSuspendedUser_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("user123");
        when(jwtUtil.extractEmail(token)).thenReturn("user@example.com");
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(jwtUtil.extractRole(token)).thenReturn("USER");
        when(jwtUtil.extractStatus(token)).thenReturn(1); // SUSPENDED

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithBannedUser_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("user123");
        when(jwtUtil.extractEmail(token)).thenReturn("user@example.com");
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(jwtUtil.extractRole(token)).thenReturn("USER");
        when(jwtUtil.extractStatus(token)).thenReturn(2); // BANNED

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithAlreadyAuthenticatedUser_ShouldNotOverrideAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        UsernamePasswordAuthenticationToken existingAuth = new UsernamePasswordAuthenticationToken("existing", null);
        SecurityContextHolder.getContext().setAuthentication(existingAuth);

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("user123");
        when(jwtUtil.extractEmail(token)).thenReturn("user@example.com");
        when(jwtUtil.extractUsername(token)).thenReturn("testuser");
        when(jwtUtil.extractRole(token)).thenReturn("USER");
        when(jwtUtil.extractStatus(token)).thenReturn(0);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertEquals(existingAuth, SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithJwtUtilException_ShouldContinueFilterChain() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenThrow(new RuntimeException("JWT validation error"));

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithNullUserId_ShouldNotSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn(null);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithAuthorRole_ShouldSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("author123");
        when(jwtUtil.extractEmail(token)).thenReturn("author@example.com");
        when(jwtUtil.extractUsername(token)).thenReturn("testauthor");
        when(jwtUtil.extractRole(token)).thenReturn("AUTHOR");
        when(jwtUtil.extractStatus(token)).thenReturn(0);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
        verify(filterChain).doFilter(request, response);
    }

    @Test
    void doFilterInternal_WithAdminRole_ShouldSetAuthentication() throws ServletException, IOException {
        // Arrange
        String token = "valid-jwt-token";
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtUtil.validateToken(token)).thenReturn(true);
        when(jwtUtil.isAccessToken(token)).thenReturn(true);
        when(jwtUtil.extractUserId(token)).thenReturn("admin123");
        when(jwtUtil.extractEmail(token)).thenReturn("admin@example.com");
        when(jwtUtil.extractUsername(token)).thenReturn("testadmin");
        when(jwtUtil.extractRole(token)).thenReturn("ADMIN");
        when(jwtUtil.extractStatus(token)).thenReturn(0);

        // Act
        filter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        assertTrue(SecurityContextHolder.getContext().getAuthentication() instanceof UsernamePasswordAuthenticationToken);
        verify(filterChain).doFilter(request, response);
    }

}
