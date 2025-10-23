package com.yushan.engagement_service.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for NovelStatus enum
 */
class NovelStatusTest {

    @Test
    void testDraft() {
        // Act & Assert
        assertEquals(0, NovelStatus.DRAFT.getValue());
        assertEquals("Draft", NovelStatus.DRAFT.getDescription());
    }

    @Test
    void testUnderReview() {
        // Act & Assert
        assertEquals(1, NovelStatus.UNDER_REVIEW.getValue());
        assertEquals("Under Review", NovelStatus.UNDER_REVIEW.getDescription());
    }

    @Test
    void testPublished() {
        // Act & Assert
        assertEquals(2, NovelStatus.PUBLISHED.getValue());
        assertEquals("Published", NovelStatus.PUBLISHED.getDescription());
    }

    @Test
    void testHidden() {
        // Act & Assert
        assertEquals(3, NovelStatus.HIDDEN.getValue());
        assertEquals("Hidden", NovelStatus.HIDDEN.getDescription());
    }

    @Test
    void testArchived() {
        // Act & Assert
        assertEquals(4, NovelStatus.ARCHIVED.getValue());
        assertEquals("Archived", NovelStatus.ARCHIVED.getDescription());
    }

    @Test
    void testEnumValues() {
        // Act
        NovelStatus[] values = NovelStatus.values();

        // Assert
        assertEquals(5, values.length);
        assertEquals(NovelStatus.DRAFT, values[0]);
        assertEquals(NovelStatus.UNDER_REVIEW, values[1]);
        assertEquals(NovelStatus.PUBLISHED, values[2]);
        assertEquals(NovelStatus.HIDDEN, values[3]);
        assertEquals(NovelStatus.ARCHIVED, values[4]);
    }

    @Test
    void testValueOf() {
        // Act & Assert
        assertEquals(NovelStatus.DRAFT, NovelStatus.valueOf("DRAFT"));
        assertEquals(NovelStatus.UNDER_REVIEW, NovelStatus.valueOf("UNDER_REVIEW"));
        assertEquals(NovelStatus.PUBLISHED, NovelStatus.valueOf("PUBLISHED"));
        assertEquals(NovelStatus.HIDDEN, NovelStatus.valueOf("HIDDEN"));
        assertEquals(NovelStatus.ARCHIVED, NovelStatus.valueOf("ARCHIVED"));
    }

    @Test
    void testValueOf_WithInvalidName_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            NovelStatus.valueOf("INVALID_STATUS");
        });
    }

    @Test
    void testFromValue_WithValidValues() {
        // Act & Assert
        assertEquals(NovelStatus.DRAFT, NovelStatus.fromValue(0));
        assertEquals(NovelStatus.UNDER_REVIEW, NovelStatus.fromValue(1));
        assertEquals(NovelStatus.PUBLISHED, NovelStatus.fromValue(2));
        assertEquals(NovelStatus.HIDDEN, NovelStatus.fromValue(3));
        assertEquals(NovelStatus.ARCHIVED, NovelStatus.fromValue(4));
    }

    @Test
    void testFromValue_WithInvalidValue_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            NovelStatus.fromValue(999);
        });
    }

    @Test
    void testFromName_WithValidNames() {
        // Act & Assert
        assertEquals(NovelStatus.DRAFT, NovelStatus.fromName("DRAFT"));
        assertEquals(NovelStatus.UNDER_REVIEW, NovelStatus.fromName("UNDER_REVIEW"));
        assertEquals(NovelStatus.PUBLISHED, NovelStatus.fromName("PUBLISHED"));
        assertEquals(NovelStatus.HIDDEN, NovelStatus.fromName("HIDDEN"));
        assertEquals(NovelStatus.ARCHIVED, NovelStatus.fromName("ARCHIVED"));
    }

    @Test
    void testFromName_WithCaseInsensitiveNames() {
        // Act & Assert
        assertEquals(NovelStatus.DRAFT, NovelStatus.fromName("draft"));
        assertEquals(NovelStatus.UNDER_REVIEW, NovelStatus.fromName("under_review"));
        assertEquals(NovelStatus.PUBLISHED, NovelStatus.fromName("Published"));
        assertEquals(NovelStatus.HIDDEN, NovelStatus.fromName("hidden"));
        assertEquals(NovelStatus.ARCHIVED, NovelStatus.fromName("ARCHIVED"));
    }

    @Test
    void testFromName_WithNullName_ReturnsNull() {
        // Act & Assert
        assertNull(NovelStatus.fromName(null));
    }

    @Test
    void testFromName_WithInvalidName_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            NovelStatus.fromName("INVALID_STATUS");
        });
    }
}
