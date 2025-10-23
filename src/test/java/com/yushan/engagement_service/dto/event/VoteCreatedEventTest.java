package com.yushan.engagement_service.dto.event;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for VoteCreatedEvent
 */
class VoteCreatedEventTest {

    @Test
    void constructor_Default_SetsNullFields() {
        // Act
        VoteCreatedEvent event = new VoteCreatedEvent();

        // Assert
        assertNull(event.getVoteId());
        assertNull(event.getUserId());
    }

    @Test
    void constructor_WithAllParameters_SetsAllFields() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        // Act
        VoteCreatedEvent event = new VoteCreatedEvent(voteId, userId);

        // Assert
        assertEquals(voteId, event.getVoteId());
        assertEquals(userId, event.getUserId());
    }

    @Test
    void builder_WithAllFields_SetsAllFields() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        // Act
        VoteCreatedEvent event = VoteCreatedEvent.builder()
                .voteId(voteId)
                .userId(userId)
                .build();

        // Assert
        assertEquals(voteId, event.getVoteId());
        assertEquals(userId, event.getUserId());
    }

    @Test
    void setters_WithValues_SetsFields() {
        // Arrange
        VoteCreatedEvent event = new VoteCreatedEvent();
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        // Act
        event.setVoteId(voteId);
        event.setUserId(userId);

        // Assert
        assertEquals(voteId, event.getVoteId());
        assertEquals(userId, event.getUserId());
    }

    @Test
    void equals_WithSameValues_ReturnsTrue() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        VoteCreatedEvent event1 = new VoteCreatedEvent(voteId, userId);
        VoteCreatedEvent event2 = new VoteCreatedEvent(voteId, userId);

        // Act & Assert
        assertEquals(event1, event2);
    }

    @Test
    void equals_WithDifferentValues_ReturnsFalse() {
        // Arrange
        Integer voteId1 = 1;
        Integer voteId2 = 2;
        UUID userId = UUID.randomUUID();

        VoteCreatedEvent event1 = new VoteCreatedEvent(voteId1, userId);
        VoteCreatedEvent event2 = new VoteCreatedEvent(voteId2, userId);

        // Act & Assert
        assertNotEquals(event1, event2);
    }

    @Test
    void hashCode_WithSameValues_ReturnsSameHashCode() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        VoteCreatedEvent event1 = new VoteCreatedEvent(voteId, userId);
        VoteCreatedEvent event2 = new VoteCreatedEvent(voteId, userId);

        // Act & Assert
        assertEquals(event1.hashCode(), event2.hashCode());
    }

    @Test
    void toString_ContainsAllFields() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        VoteCreatedEvent event = new VoteCreatedEvent(voteId, userId);

        // Act
        String toString = event.toString();

        // Assert
        assertTrue(toString.contains(voteId.toString()));
        assertTrue(toString.contains(userId.toString()));
    }
}
