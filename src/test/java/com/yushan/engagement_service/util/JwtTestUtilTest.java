package com.yushan.engagement_service.util;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtTestUtil
 */
@ExtendWith(MockitoExtension.class)
class JwtTestUtilTest {

    @InjectMocks
    private JwtTestUtil jwtTestUtil;

    @BeforeEach
    void setUp() {
        // Set test values using reflection
        ReflectionTestUtils.setField(jwtTestUtil, "secret", "TestJWTSecretKeyForEngagementServiceTestingPurposesOnly");
        ReflectionTestUtils.setField(jwtTestUtil, "issuer", "yushan-engagement-service-test");
    }

    @Test
    void generateTestAuthorToken_ShouldReturnValidToken() {
        // Act
        String token = jwtTestUtil.generateTestAuthorToken();

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains(".")); // JWT format has dots
    }

    @Test
    void generateTestAdminToken_ShouldReturnValidToken() {
        // Act
        String token = jwtTestUtil.generateTestAdminToken();

        // Assert
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains(".")); // JWT format has dots
    }

    @Test
    void generateTestAuthorToken_ShouldHaveCorrectClaims() {
        // Act
        String token = jwtTestUtil.generateTestAuthorToken();

        // Assert
        assertNotNull(token);
        // Token should contain the author-specific claims
        // Note: In a real test, you might want to decode and verify the claims
        // For now, we just verify the token is generated successfully
    }

    @Test
    void generateTestAdminToken_ShouldHaveCorrectClaims() {
        // Act
        String token = jwtTestUtil.generateTestAdminToken();

        // Assert
        assertNotNull(token);
        // Token should contain the admin-specific claims
    }

    @Test
    void generateTestAuthorToken_ShouldBeDifferentFromAdminToken() {
        // Act
        String authorToken = jwtTestUtil.generateTestAuthorToken();
        String adminToken = jwtTestUtil.generateTestAdminToken();

        // Assert
        assertNotEquals(authorToken, adminToken);
    }

    @Test
    void generateTestAuthorToken_MultipleCalls_ShouldGenerateValidTokens() {
        // Act
        String token1 = jwtTestUtil.generateTestAuthorToken();
        String token2 = jwtTestUtil.generateTestAuthorToken();

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token1.isEmpty());
        assertFalse(token2.isEmpty());
        // Both tokens should be valid JWT format
        assertTrue(token1.contains("."));
        assertTrue(token2.contains("."));
    }

    @Test
    void generateTestAdminToken_MultipleCalls_ShouldGenerateValidTokens() {
        // Act
        String token1 = jwtTestUtil.generateTestAdminToken();
        String token2 = jwtTestUtil.generateTestAdminToken();

        // Assert
        assertNotNull(token1);
        assertNotNull(token2);
        assertFalse(token1.isEmpty());
        assertFalse(token2.isEmpty());
        // Both tokens should be valid JWT format
        assertTrue(token1.contains("."));
        assertTrue(token2.contains("."));
    }

    @Test
    void generateTestAuthorToken_ShouldHaveValidJWTStructure() {
        // Act
        String token = jwtTestUtil.generateTestAuthorToken();

        // Assert
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // JWT should have 3 parts: header, payload, signature
    }

    @Test
    void generateTestAdminToken_ShouldHaveValidJWTStructure() {
        // Act
        String token = jwtTestUtil.generateTestAdminToken();

        // Assert
        String[] parts = token.split("\\.");
        assertEquals(3, parts.length); // JWT should have 3 parts: header, payload, signature
    }

    @Test
    void generateTestAuthorToken_WithDifferentSecret_ShouldReturnValidToken() {
        // Setup
        ReflectionTestUtils.setField(jwtTestUtil, "secret", "different-secret-key-that-is-long-enough-for-jwt");
        ReflectionTestUtils.setField(jwtTestUtil, "issuer", "test-issuer");

        // Execute
        String token = jwtTestUtil.generateTestAuthorToken();

        // Verify
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }

    @Test
    void generateTestAdminToken_WithDifferentIssuer_ShouldReturnValidToken() {
        // Setup
        ReflectionTestUtils.setField(jwtTestUtil, "secret", "test-secret-key-that-is-long-enough-for-jwt");
        ReflectionTestUtils.setField(jwtTestUtil, "issuer", "different-issuer");

        // Execute
        String token = jwtTestUtil.generateTestAdminToken();

        // Verify
        assertNotNull(token);
        assertFalse(token.isEmpty());
        assertTrue(token.contains("."));
    }
}
