package com.yushan.engagement_service.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for JwtUtil
 */
@ExtendWith(MockitoExtension.class)
class JwtUtilTest {

    @InjectMocks
    private JwtUtil jwtUtil;

    private String testSecret;
    private String testIssuer;
    private String validToken;
    private String expiredToken;
    private String invalidToken;

    @BeforeEach
    void setUp() {
        testSecret = "TestJWTSecretKeyForEngagementServiceTestingPurposesOnly";
        testIssuer = "yushan-engagement-service-test";
        
        // Set private fields using reflection
        ReflectionTestUtils.setField(jwtUtil, "secret", testSecret);
        ReflectionTestUtils.setField(jwtUtil, "issuer", testIssuer);

        // Create valid token
        validToken = createTestToken("550e8400-e29b-41d4-a716-446655440001", 
            "test@example.com", "test_user", "AUTHOR", 0, "access", 
            new Date(System.currentTimeMillis() + 3600000)); // 1 hour from now

        // Create expired token
        expiredToken = createTestToken("550e8400-e29b-41d4-a716-446655440001", 
            "test@example.com", "test_user", "AUTHOR", 0, "access", 
            new Date(System.currentTimeMillis() - 3600000)); // 1 hour ago

        // Create invalid token
        invalidToken = "invalid.token.here";
    }

    private String createTestToken(String userId, String email, String username, 
                                 String role, Integer status, String tokenType, Date expiration) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("username", username);
        claims.put("role", role);
        claims.put("status", status);
        claims.put("tokenType", tokenType);

        byte[] keyBytes = testSecret.getBytes(StandardCharsets.UTF_8);
        SecretKey key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .claims(claims)
                .subject(email)
                .issuer(testIssuer)
                .issuedAt(new Date())
                .expiration(expiration)
                .signWith(key)
                .compact();
    }

    @Test
    void extractUserId_WithValidToken_ReturnsUserId() {
        // Act
        String result = jwtUtil.extractUserId(validToken);

        // Assert
        assertEquals("550e8400-e29b-41d4-a716-446655440001", result);
    }

    @Test
    void extractEmail_WithValidToken_ReturnsEmail() {
        // Act
        String result = jwtUtil.extractEmail(validToken);

        // Assert
        assertEquals("test@example.com", result);
    }

    @Test
    void extractRole_WithValidToken_ReturnsRole() {
        // Act
        String result = jwtUtil.extractRole(validToken);

        // Assert
        assertEquals("AUTHOR", result);
    }

    @Test
    void extractStatus_WithValidToken_ReturnsStatus() {
        // Act
        Integer result = jwtUtil.extractStatus(validToken);

        // Assert
        assertEquals(0, result);
    }

    @Test
    void extractUsername_WithValidToken_ReturnsUsername() {
        // Act
        String result = jwtUtil.extractUsername(validToken);

        // Assert
        assertEquals("test_user", result);
    }

    @Test
    void extractTokenType_WithValidToken_ReturnsTokenType() {
        // Act
        String result = jwtUtil.extractTokenType(validToken);

        // Assert
        assertEquals("access", result);
    }

    @Test
    void extractExpiration_WithValidToken_ReturnsExpiration() {
        // Act
        Date result = jwtUtil.extractExpiration(validToken);

        // Assert
        assertNotNull(result);
        assertTrue(result.after(new Date()));
    }

    @Test
    void extractClaim_WithValidToken_ReturnsClaim() {
        // Act
        String result = jwtUtil.extractClaim(validToken, claims -> claims.get("userId", String.class));

        // Assert
        assertEquals("550e8400-e29b-41d4-a716-446655440001", result);
    }

    @Test
    void extractAllClaims_WithValidToken_ReturnsClaims() {
        // Act
        Claims result = jwtUtil.extractAllClaims(validToken);

        // Assert
        assertNotNull(result);
        assertEquals("550e8400-e29b-41d4-a716-446655440001", result.get("userId"));
        assertEquals("test@example.com", result.get("email"));
        assertEquals("test_user", result.get("username"));
        assertEquals("AUTHOR", result.get("role"));
        assertEquals(0, result.get("status"));
        assertEquals("access", result.get("tokenType"));
    }

    @Test
    void isTokenExpired_WithValidToken_ReturnsFalse() {
        // Act
        Boolean result = jwtUtil.isTokenExpired(validToken);

        // Assert
        assertFalse(result);
    }

    @Test
    void isTokenExpired_WithExpiredToken_ThrowsException() {
        // Act & Assert
        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            jwtUtil.isTokenExpired(expiredToken);
        });
    }

    @Test
    void validateToken_WithValidToken_ReturnsTrue() {
        // Act
        Boolean result = jwtUtil.validateToken(validToken);

        // Assert
        assertTrue(result);
    }

    @Test
    void validateToken_WithExpiredToken_ReturnsFalse() {
        // Act
        Boolean result = jwtUtil.validateToken(expiredToken);

        // Assert
        assertFalse(result);
    }

    @Test
    void validateToken_WithInvalidToken_ReturnsFalse() {
        // Act
        Boolean result = jwtUtil.validateToken(invalidToken);

        // Assert
        assertFalse(result);
    }

    @Test
    void validateToken_WithNullToken_ReturnsFalse() {
        // Act
        Boolean result = jwtUtil.validateToken(null);

        // Assert
        assertFalse(result);
    }

    @Test
    void validateToken_WithEmptyToken_ReturnsFalse() {
        // Act
        Boolean result = jwtUtil.validateToken("");

        // Assert
        assertFalse(result);
    }

    @Test
    void isAccessToken_WithAccessToken_ReturnsTrue() {
        // Act
        Boolean result = jwtUtil.isAccessToken(validToken);

        // Assert
        assertTrue(result);
    }

    @Test
    void isAccessToken_WithRefreshToken_ReturnsFalse() {
        // Arrange
        String refreshToken = createTestToken("550e8400-e29b-41d4-a716-446655440001", 
            "test@example.com", "test_user", "AUTHOR", 0, "refresh", 
            new Date(System.currentTimeMillis() + 3600000));

        // Act
        Boolean result = jwtUtil.isAccessToken(refreshToken);

        // Assert
        assertFalse(result);
    }

    @Test
    void isRefreshToken_WithRefreshToken_ReturnsTrue() {
        // Arrange
        String refreshToken = createTestToken("550e8400-e29b-41d4-a716-446655440001", 
            "test@example.com", "test_user", "AUTHOR", 0, "refresh", 
            new Date(System.currentTimeMillis() + 3600000));

        // Act
        Boolean result = jwtUtil.isRefreshToken(refreshToken);

        // Assert
        assertTrue(result);
    }

    @Test
    void isRefreshToken_WithAccessToken_ReturnsFalse() {
        // Act
        Boolean result = jwtUtil.isRefreshToken(validToken);

        // Assert
        assertFalse(result);
    }

    @Test
    void extractUserId_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractUserId(invalidToken));
    }

    @Test
    void extractEmail_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractEmail(invalidToken));
    }

    @Test
    void extractRole_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractRole(invalidToken));
    }

    @Test
    void extractStatus_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractStatus(invalidToken));
    }

    @Test
    void extractUsername_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractUsername(invalidToken));
    }

    @Test
    void extractTokenType_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractTokenType(invalidToken));
    }

    @Test
    void extractExpiration_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractExpiration(invalidToken));
    }

    @Test
    void extractAllClaims_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractAllClaims(invalidToken));
    }

    @Test
    void isTokenExpired_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.isTokenExpired(invalidToken));
    }

    @Test
    void isAccessToken_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.isAccessToken(invalidToken));
    }

    @Test
    void isRefreshToken_WithInvalidToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.isRefreshToken(invalidToken));
    }

    @Test
    void extractClaim_WithNullToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractClaim(null, Claims::getSubject));
    }

    @Test
    void extractClaim_WithEmptyToken_ThrowsException() {
        // Act & Assert
        assertThrows(Exception.class, () -> jwtUtil.extractClaim("", Claims::getSubject));
    }

    @Test
    void getSigningKey_ReturnsCorrectKey() {
        // This test verifies that the private method works correctly
        // by testing its behavior through public methods
        String result = jwtUtil.extractUserId(validToken);
        assertNotNull(result);
        assertEquals("550e8400-e29b-41d4-a716-446655440001", result);
    }

    @Test
    void extractClaim_WithCustomFunction_ReturnsCorrectValue() {
        // Act
        String result = jwtUtil.extractClaim(validToken, claims -> 
            claims.get("userId", String.class) + ":" + claims.get("email", String.class));

        // Assert
        assertEquals("550e8400-e29b-41d4-a716-446655440001:test@example.com", result);
    }

    @Test
    void extractClaim_WithNullClaim_ReturnsNull() {
        // Arrange
        String tokenWithNullClaim = createTestToken(null, "test@example.com", "test_user", "AUTHOR", 0, "access", 
            new Date(System.currentTimeMillis() + 3600000));

        // Act
        String result = jwtUtil.extractUserId(tokenWithNullClaim);

        // Assert
        assertNull(result);
    }
}
