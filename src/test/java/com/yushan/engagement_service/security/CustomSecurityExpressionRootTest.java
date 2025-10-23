package com.yushan.engagement_service.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomSecurityExpressionRoot
 */
class CustomSecurityExpressionRootTest {

    private CustomSecurityExpressionRoot expressionRoot;
    private CustomUserDetails userDetails;

    @BeforeEach
    void setUp() {
        userDetails = new CustomUserDetails(
            UUID.randomUUID().toString(),
            "test@example.com",
            "testuser",
            "AUTHOR",
            0
        );

        Authentication authentication = new UsernamePasswordAuthenticationToken(
            userDetails, null, Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_AUTHOR"))
        );

        // Set SecurityContext for CustomSecurityExpressionRoot to use
        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        expressionRoot = new CustomSecurityExpressionRoot(authentication);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void hasRoleCustom_WithMatchingRole_ReturnsTrue() {
        // Act & Assert
        assertTrue(expressionRoot.hasRoleCustom("AUTHOR"));
    }

    @Test
    void hasRoleCustom_WithNonMatchingRole_ReturnsFalse() {
        // Act & Assert
        assertFalse(expressionRoot.hasRoleCustom("ADMIN"));
    }

    @Test
    void hasAnyRoleCustom_WithMatchingRole_ReturnsTrue() {
        // Act & Assert
        assertTrue(expressionRoot.hasAnyRoleCustom("AUTHOR", "ADMIN"));
    }

    @Test
    void hasAnyRoleCustom_WithNonMatchingRoles_ReturnsFalse() {
        // Act & Assert
        assertFalse(expressionRoot.hasAnyRoleCustom("USER", "MODERATOR"));
    }

    @Test
    void isAuthenticatedCustom_WithValidUser_ReturnsTrue() {
        // Act & Assert
        assertTrue(expressionRoot.isAuthenticatedCustom());
    }

    @Test
    void isAuthor_WithAuthorRole_ReturnsTrue() {
        // Act & Assert
        assertTrue(expressionRoot.isAuthor());
    }

    @Test
    void isAuthor_WithNonAuthorRole_ReturnsFalse() {
        // Arrange
        CustomUserDetails adminUser = new CustomUserDetails(
            UUID.randomUUID().toString(),
            "admin@example.com",
            "admin",
            "ADMIN",
            0
        );

        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
            adminUser, null, Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        // Set SecurityContext for admin user
        SecurityContext adminContext = SecurityContextHolder.createEmptyContext();
        adminContext.setAuthentication(adminAuth);
        SecurityContextHolder.setContext(adminContext);

        CustomSecurityExpressionRoot adminExpressionRoot = new CustomSecurityExpressionRoot(adminAuth);

        // Act & Assert
        assertFalse(adminExpressionRoot.isAuthor());
    }

    @Test
    void isAdmin_WithAdminRole_ReturnsTrue() {
        // Arrange
        CustomUserDetails adminUser = new CustomUserDetails(
            UUID.randomUUID().toString(),
            "admin@example.com",
            "admin",
            "ADMIN",
            0
        );

        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
            adminUser, null, Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        // Set SecurityContext for admin user
        SecurityContext adminContext = SecurityContextHolder.createEmptyContext();
        adminContext.setAuthentication(adminAuth);
        SecurityContextHolder.setContext(adminContext);

        CustomSecurityExpressionRoot adminExpressionRoot = new CustomSecurityExpressionRoot(adminAuth);

        // Act & Assert
        assertTrue(adminExpressionRoot.isAdmin());
    }

    @Test
    void isAdmin_WithNonAdminRole_ReturnsFalse() {
        // Act & Assert
        assertFalse(expressionRoot.isAdmin());
    }

    @Test
    void isAuthorOrAdmin_WithAuthorRole_ReturnsTrue() {
        // Act & Assert
        assertTrue(expressionRoot.isAuthorOrAdmin());
    }

    @Test
    void isAuthorOrAdmin_WithAdminRole_ReturnsTrue() {
        // Arrange
        CustomUserDetails adminUser = new CustomUserDetails(
            UUID.randomUUID().toString(),
            "admin@example.com",
            "admin",
            "ADMIN",
            0
        );

        Authentication adminAuth = new UsernamePasswordAuthenticationToken(
            adminUser, null, Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_ADMIN"))
        );

        // Set SecurityContext for admin user
        SecurityContext adminContext = SecurityContextHolder.createEmptyContext();
        adminContext.setAuthentication(adminAuth);
        SecurityContextHolder.setContext(adminContext);

        CustomSecurityExpressionRoot adminExpressionRoot = new CustomSecurityExpressionRoot(adminAuth);

        // Act & Assert
        assertTrue(adminExpressionRoot.isAuthorOrAdmin());
    }

    @Test
    void isAuthorOrAdmin_WithUserRole_ReturnsFalse() {
        // Arrange
        CustomUserDetails userUser = new CustomUserDetails(
            UUID.randomUUID().toString(),
            "user@example.com",
            "user",
            "USER",
            0
        );

        Authentication userAuth = new UsernamePasswordAuthenticationToken(
            userUser, null, Collections.singletonList(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_USER"))
        );

        // Set SecurityContext for user user
        SecurityContext userContext = SecurityContextHolder.createEmptyContext();
        userContext.setAuthentication(userAuth);
        SecurityContextHolder.setContext(userContext);

        CustomSecurityExpressionRoot userExpressionRoot = new CustomSecurityExpressionRoot(userAuth);

        // Act & Assert
        assertFalse(userExpressionRoot.isAuthorOrAdmin());
    }

    @Test
    void hasRoleCustom_WithNullAuthentication_ReturnsFalse() {
        // Arrange
        SecurityContextHolder.clearContext();
        CustomSecurityExpressionRoot nullAuthExpressionRoot = new CustomSecurityExpressionRoot(null);

        // Act & Assert
        assertFalse(nullAuthExpressionRoot.hasRoleCustom("AUTHOR"));
    }

    @Test
    void hasAnyRoleCustom_WithNullAuthentication_ReturnsFalse() {
        // Arrange
        SecurityContextHolder.clearContext();
        CustomSecurityExpressionRoot nullAuthExpressionRoot = new CustomSecurityExpressionRoot(null);

        // Act & Assert
        assertFalse(nullAuthExpressionRoot.hasAnyRoleCustom("AUTHOR", "ADMIN"));
    }

    @Test
    void isAuthenticatedCustom_WithNullAuthentication_ReturnsFalse() {
        // Arrange
        SecurityContextHolder.clearContext();
        CustomSecurityExpressionRoot nullAuthExpressionRoot = new CustomSecurityExpressionRoot(null);

        // Act & Assert
        assertFalse(nullAuthExpressionRoot.isAuthenticatedCustom());
    }

    @Test
    void setFilterObject_SetsFilterObject() {
        // Arrange
        Object filterObject = "test";

        // Act
        expressionRoot.setFilterObject(filterObject);

        // Assert
        assertEquals(filterObject, expressionRoot.getFilterObject());
    }

    @Test
    void setReturnObject_SetsReturnObject() {
        // Arrange
        Object returnObject = "result";

        // Act
        expressionRoot.setReturnObject(returnObject);

        // Assert
        assertEquals(returnObject, expressionRoot.getReturnObject());
    }

    @Test
    void getThis_ReturnsThis() {
        // Act & Assert
        assertEquals(expressionRoot, expressionRoot.getThis());
    }
}
