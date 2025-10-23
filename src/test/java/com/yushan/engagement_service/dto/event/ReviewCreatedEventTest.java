package com.yushan.engagement_service.dto.event;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReviewCreatedEvent
 */
class ReviewCreatedEventTest {

    @Test
    void constructor_Default_SetsNullFields() {
        // Act
        ReviewCreatedEvent event = new ReviewCreatedEvent();

        // Assert
        assertNull(event.getReviewId());
        assertNull(event.getUserId());
        assertNull(event.getRating());
    }

    @Test
    void constructor_WithAllParameters_SetsAllFields() {
        // Arrange
        Integer reviewId = 1;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        // Act
        ReviewCreatedEvent event = new ReviewCreatedEvent(reviewId, userId, rating);

        // Assert
        assertEquals(reviewId, event.getReviewId());
        assertEquals(userId, event.getUserId());
        assertEquals(rating, event.getRating());
    }

    @Test
    void builder_WithAllFields_SetsAllFields() {
        // Arrange
        Integer reviewId = 1;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        // Act
        ReviewCreatedEvent event = ReviewCreatedEvent.builder()
                .reviewId(reviewId)
                .userId(userId)
                .rating(rating)
                .build();

        // Assert
        assertEquals(reviewId, event.getReviewId());
        assertEquals(userId, event.getUserId());
        assertEquals(rating, event.getRating());
    }

    @Test
    void setters_WithValues_SetsFields() {
        // Arrange
        ReviewCreatedEvent event = new ReviewCreatedEvent();
        Integer reviewId = 1;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        // Act
        event.setReviewId(reviewId);
        event.setUserId(userId);
        event.setRating(rating);

        // Assert
        assertEquals(reviewId, event.getReviewId());
        assertEquals(userId, event.getUserId());
        assertEquals(rating, event.getRating());
    }

    @Test
    void equals_WithSameValues_ReturnsTrue() {
        // Arrange
        Integer reviewId = 1;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        ReviewCreatedEvent event1 = new ReviewCreatedEvent(reviewId, userId, rating);
        ReviewCreatedEvent event2 = new ReviewCreatedEvent(reviewId, userId, rating);

        // Act & Assert
        assertEquals(event1, event2);
    }

    @Test
    void equals_WithDifferentValues_ReturnsFalse() {
        // Arrange
        Integer reviewId1 = 1;
        Integer reviewId2 = 2;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        ReviewCreatedEvent event1 = new ReviewCreatedEvent(reviewId1, userId, rating);
        ReviewCreatedEvent event2 = new ReviewCreatedEvent(reviewId2, userId, rating);

        // Act & Assert
        assertNotEquals(event1, event2);
    }

    @Test
    void hashCode_WithSameValues_ReturnsSameHashCode() {
        // Arrange
        Integer reviewId = 1;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        ReviewCreatedEvent event1 = new ReviewCreatedEvent(reviewId, userId, rating);
        ReviewCreatedEvent event2 = new ReviewCreatedEvent(reviewId, userId, rating);

        // Act & Assert
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        // Arrange
        Integer reviewId = 1;
        UUID userId = UUID.randomUUID();
        Integer rating = 5;

        ReviewCreatedEvent event = new ReviewCreatedEvent(reviewId, userId, rating);

        // Act
        String toString = event.toString();

        // Assert
        assertTrue(toString.contains(reviewId.toString()));
        assertTrue(toString.contains(userId.toString()));
        assertTrue(toString.contains(rating.toString()));
    }
}
