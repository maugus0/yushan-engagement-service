package com.yushan.engagement_service.dto.event;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserActivityEvent
 */
class UserActivityEventTest {

    @Test
    void constructor_WithAllParameters_SetsAllFields() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String serviceName = "engagement-service";
        String endpoint = "/api/v1/comments";
        String method = "POST";
        LocalDateTime timestamp = LocalDateTime.now();

        // Act
        UserActivityEvent event = new UserActivityEvent(userId, serviceName, endpoint, method, timestamp);

        // Assert
        assertEquals(userId, event.userId());
        assertEquals(serviceName, event.serviceName());
        assertEquals(endpoint, event.endpoint());
        assertEquals(method, event.method());
        assertEquals(timestamp, event.timestamp());
    }

    @Test
    void constructor_WithNullValues_SetsNullFields() {
        // Act
        UserActivityEvent event = new UserActivityEvent(null, null, null, null, null);

        // Assert
        assertNull(event.userId());
        assertNull(event.serviceName());
        assertNull(event.endpoint());
        assertNull(event.method());
        assertNull(event.timestamp());
    }

    @Test
    void equals_WithSameValues_ReturnsTrue() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String serviceName = "engagement-service";
        String endpoint = "/api/v1/comments";
        String method = "POST";
        LocalDateTime timestamp = LocalDateTime.now();

        UserActivityEvent event1 = new UserActivityEvent(userId, serviceName, endpoint, method, timestamp);
        UserActivityEvent event2 = new UserActivityEvent(userId, serviceName, endpoint, method, timestamp);

        // Act & Assert
        assertEquals(event1, event2);
    }

    @Test
    void equals_WithDifferentValues_ReturnsFalse() {
        // Arrange
        UUID userId1 = UUID.randomUUID();
        UUID userId2 = UUID.randomUUID();
        String serviceName = "engagement-service";
        String endpoint = "/api/v1/comments";
        String method = "POST";
        LocalDateTime timestamp = LocalDateTime.now();

        UserActivityEvent event1 = new UserActivityEvent(userId1, serviceName, endpoint, method, timestamp);
        UserActivityEvent event2 = new UserActivityEvent(userId2, serviceName, endpoint, method, timestamp);

        // Act & Assert
        assertNotEquals(event1, event2);
    }

    @Test
    void hashCode_WithSameValues_ReturnsSameHashCode() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String serviceName = "engagement-service";
        String endpoint = "/api/v1/comments";
        String method = "POST";
        LocalDateTime timestamp = LocalDateTime.now();

        UserActivityEvent event1 = new UserActivityEvent(userId, serviceName, endpoint, method, timestamp);
        UserActivityEvent event2 = new UserActivityEvent(userId, serviceName, endpoint, method, timestamp);

        // Act & Assert
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        // Arrange
        UUID userId = UUID.randomUUID();
        String serviceName = "engagement-service";
        String endpoint = "/api/v1/comments";
        String method = "POST";
        LocalDateTime timestamp = LocalDateTime.now();

        UserActivityEvent event = new UserActivityEvent(userId, serviceName, endpoint, method, timestamp);

        // Act
        String toString = event.toString();

        // Assert
        assertTrue(toString.contains(userId.toString()));
        assertTrue(toString.contains(serviceName));
        assertTrue(toString.contains(endpoint));
        assertTrue(toString.contains(method));
        assertTrue(toString.contains(timestamp.toString()));
    }
}
