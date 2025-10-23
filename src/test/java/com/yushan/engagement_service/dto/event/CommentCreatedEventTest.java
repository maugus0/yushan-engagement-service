package com.yushan.engagement_service.dto.event;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CommentCreatedEvent
 */
class CommentCreatedEventTest {

    @Test
    void constructor_Default_SetsNullFields() {
        // Act
        CommentCreatedEvent event = new CommentCreatedEvent();

        // Assert
        assertNull(event.getCommentId());
        assertNull(event.getUserId());
    }

    @Test
    void constructor_WithAllParameters_SetsAllFields() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();

        // Act
        CommentCreatedEvent event = new CommentCreatedEvent(commentId, userId);

        // Assert
        assertEquals(commentId, event.getCommentId());
        assertEquals(userId, event.getUserId());
    }

    @Test
    void builder_WithAllFields_SetsAllFields() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();

        // Act
        CommentCreatedEvent event = CommentCreatedEvent.builder()
                .commentId(commentId)
                .userId(userId)
                .build();

        // Assert
        assertEquals(commentId, event.getCommentId());
        assertEquals(userId, event.getUserId());
    }

    @Test
    void setters_WithValues_SetsFields() {
        // Arrange
        CommentCreatedEvent event = new CommentCreatedEvent();
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();

        // Act
        event.setCommentId(commentId);
        event.setUserId(userId);

        // Assert
        assertEquals(commentId, event.getCommentId());
        assertEquals(userId, event.getUserId());
    }

    @Test
    void equals_WithSameValues_ReturnsTrue() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();

        CommentCreatedEvent event1 = new CommentCreatedEvent(commentId, userId);
        CommentCreatedEvent event2 = new CommentCreatedEvent(commentId, userId);

        // Act & Assert
        assertEquals(event1, event2);
    }

    @Test
    void equals_WithDifferentValues_ReturnsFalse() {
        // Arrange
        Integer commentId1 = 1;
        Integer commentId2 = 2;
        UUID userId = UUID.randomUUID();

        CommentCreatedEvent event1 = new CommentCreatedEvent(commentId1, userId);
        CommentCreatedEvent event2 = new CommentCreatedEvent(commentId2, userId);

        // Act & Assert
        assertNotEquals(event1, event2);
    }

    @Test
    void hashCode_WithSameValues_ReturnsSameHashCode() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();

        CommentCreatedEvent event1 = new CommentCreatedEvent(commentId, userId);
        CommentCreatedEvent event2 = new CommentCreatedEvent(commentId, userId);

        // Act & Assert
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();

        CommentCreatedEvent event = new CommentCreatedEvent(commentId, userId);

        // Act
        String toString = event.toString();

        // Assert
        assertTrue(toString.contains(commentId.toString()));
        assertTrue(toString.contains(userId.toString()));
    }
}
