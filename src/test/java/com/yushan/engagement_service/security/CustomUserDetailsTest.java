package com.yushan.engagement_service.security;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CustomUserDetails
 */
class CustomUserDetailsTest {

    @Test
    void constructor_WithThreeParameters_SetsFields() {
        // Arrange
        String userId = "user123";
        String email = "test@example.com";
        String role = "AUTHOR";

        // Act
        CustomUserDetails userDetails = new CustomUserDetails(userId, email, role);

        // Assert
        assertEquals(userId, userDetails.getUserId());
        assertEquals(email, userDetails.getEmail());
        assertEquals(role, userDetails.getRole());
        assertEquals(0, userDetails.getStatus());
        assertEquals(email, userDetails.getDisplayUsername()); // Should return email as fallback
    }

    @Test
    void constructor_WithFourParameters_SetsFields() {
        // Arrange
        String userId = "user123";
        String email = "test@example.com";
        String role = "AUTHOR";
        Integer status = 1;

        // Act
        CustomUserDetails userDetails = new CustomUserDetails(userId, email, role, status);

        // Assert
        assertEquals(userId, userDetails.getUserId());
        assertEquals(email, userDetails.getEmail());
        assertEquals(role, userDetails.getRole());
        assertEquals(status, userDetails.getStatus());
        assertEquals(email, userDetails.getDisplayUsername()); // Should return email as fallback
    }

    @Test
    void constructor_WithFiveParameters_SetsFields() {
        // Arrange
        String userId = "user123";
        String email = "test@example.com";
        String username = "testuser";
        String role = "AUTHOR";
        Integer status = 0;

        // Act
        CustomUserDetails userDetails = new CustomUserDetails(userId, email, username, role, status);

        // Assert
        assertEquals(userId, userDetails.getUserId());
        assertEquals(email, userDetails.getEmail());
        assertEquals(username, userDetails.getDisplayUsername());
        assertEquals(role, userDetails.getRole());
        assertEquals(status, userDetails.getStatus());
    }

    @Test
    void getAuthorities_WithRole_ReturnsCorrectAuthorities() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR");

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertEquals(2, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_AUTHOR")));
    }

    @Test
    void getAuthorities_WithNullRole_ReturnsOnlyUserAuthority() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", null);

        // Act
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();

        // Assert
        assertEquals(1, authorities.size());
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_USER")));
    }

    @Test
    void getPassword_ReturnsNull() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR");

        // Act & Assert
        assertNull(userDetails.getPassword());
    }

    @Test
    void getUsername_ReturnsEmail() {
        // Arrange
        String email = "test@example.com";
        CustomUserDetails userDetails = new CustomUserDetails("user123", email, "AUTHOR");

        // Act & Assert
        assertEquals(email, userDetails.getUsername());
    }

    @Test
    void isAccountNonExpired_ReturnsTrue() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR");

        // Act & Assert
        assertTrue(userDetails.isAccountNonExpired());
    }

    @Test
    void isAccountNonLocked_ReturnsTrue() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR");

        // Act & Assert
        assertTrue(userDetails.isAccountNonLocked());
    }

    @Test
    void isCredentialsNonExpired_ReturnsTrue() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR");

        // Act & Assert
        assertTrue(userDetails.isCredentialsNonExpired());
    }

    @Test
    void isEnabled_WithNormalStatus_ReturnsTrue() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR", 0);

        // Act & Assert
        assertTrue(userDetails.isEnabled());
    }

    @Test
    void isEnabled_WithSuspendedStatus_ReturnsFalse() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR", 1);

        // Act & Assert
        assertFalse(userDetails.isEnabled());
    }

    @Test
    void isEnabled_WithNullStatus_ReturnsFalse() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR", null);

        // Act & Assert
        assertFalse(userDetails.isEnabled());
    }

    @Test
    void getDisplayUsername_WithUsername_ReturnsUsername() {
        // Arrange
        String username = "testuser";
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", username, "AUTHOR", 0);

        // Act & Assert
        assertEquals(username, userDetails.getDisplayUsername());
    }

    @Test
    void getDisplayUsername_WithNullUsername_ReturnsEmail() {
        // Arrange
        String email = "test@example.com";
        CustomUserDetails userDetails = new CustomUserDetails("user123", email, null, "AUTHOR", 0);

        // Act & Assert
        assertEquals(email, userDetails.getDisplayUsername());
    }

    @Test
    void isAuthor_WithAuthorRole_ReturnsTrue() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "AUTHOR");

        // Act & Assert
        assertTrue(userDetails.isAuthor());
    }

    @Test
    void isAuthor_WithNonAuthorRole_ReturnsFalse() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "USER");

        // Act & Assert
        assertFalse(userDetails.isAuthor());
    }

    @Test
    void isAdmin_WithAdminRole_ReturnsTrue() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "ADMIN");

        // Act & Assert
        assertTrue(userDetails.isAdmin());
    }

    @Test
    void isAdmin_WithNonAdminRole_ReturnsFalse() {
        // Arrange
        CustomUserDetails userDetails = new CustomUserDetails("user123", "test@example.com", "USER");

        // Act & Assert
        assertFalse(userDetails.isAdmin());
    }
}
