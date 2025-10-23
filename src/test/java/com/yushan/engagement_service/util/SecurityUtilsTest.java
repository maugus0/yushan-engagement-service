package com.yushan.engagement_service.util;

import com.yushan.engagement_service.security.CustomUserDetails;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SecurityUtils
 */
class SecurityUtilsTest {

    private SecurityContext originalContext;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        // Save original context
        originalContext = SecurityContextHolder.getContext();
        testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    }

    @AfterEach
    void tearDown() {
        // Restore original context
        SecurityContextHolder.setContext(originalContext);
    }

    @Test
    void getCurrentUserId_WithValidCustomUserDetails_ReturnsUserId() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails(
            testUserId.toString(),
            "test@example.com",
            "test_user",
            "AUTHOR",
            0
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNotNull(result);
        assertEquals(testUserId, result);
    }

    @Test
    void getCurrentUserId_WithNullAuthentication_ReturnsNull() {
        // Arrange
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(null);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithUnauthenticatedUser_ReturnsNull() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(false);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithNonCustomUserDetailsPrincipal_ReturnsNull() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn("some_other_principal");

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithNullPrincipal_ReturnsNull() {
        // Arrange
        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(null);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithInvalidUserIdFormat_ReturnsNull() {
        // Arrange
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn("invalid-uuid-format");

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithNullUserId_ReturnsNull() {
        // Arrange
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenReturn(null);

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithEmptySecurityContext_ReturnsNull() {
        // Arrange
        SecurityContext emptyContext = SecurityContextHolder.createEmptyContext();
        SecurityContextHolder.setContext(emptyContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithExceptionInGetUserId_ReturnsNull() {
        // Arrange
        CustomUserDetails userDetails = mock(CustomUserDetails.class);
        when(userDetails.getUserId()).thenThrow(new RuntimeException("Test exception"));

        Authentication authentication = mock(Authentication.class);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(userDetails);

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNull(result);
    }

    @Test
    void getCurrentUserId_WithDifferentUserRoles_ReturnsCorrectUserId() {
        // Arrange
        UUID adminUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440002");
        
        CustomUserDetails adminUserDetails = new CustomUserDetails(
            adminUserId.toString(),
            "admin@example.com",
            "admin_user",
            "ADMIN",
            0
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            adminUserDetails, null, adminUserDetails.getAuthorities()
        );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Act
        UUID result = SecurityUtils.getCurrentUserId();

        // Assert
        assertNotNull(result);
        assertEquals(adminUserId, result);
    }
}
