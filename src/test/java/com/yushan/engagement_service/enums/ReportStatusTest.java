package com.yushan.engagement_service.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportStatus enum
 */
class ReportStatusTest {

    @Test
    void testInReview() {
        // Act & Assert
        assertEquals("Under review", ReportStatus.IN_REVIEW.getDescription());
    }

    @Test
    void testResolved() {
        // Act & Assert
        assertEquals("Resolved", ReportStatus.RESOLVED.getDescription());
    }

    @Test
    void testDismissed() {
        // Act & Assert
        assertEquals("Dismissed", ReportStatus.DISMISSED.getDescription());
    }

    @Test
    void testEnumValues() {
        // Act
        ReportStatus[] values = ReportStatus.values();

        // Assert
        assertEquals(3, values.length);
        assertEquals(ReportStatus.IN_REVIEW, values[0]);
        assertEquals(ReportStatus.RESOLVED, values[1]);
        assertEquals(ReportStatus.DISMISSED, values[2]);
    }

    @Test
    void testValueOf() {
        // Act & Assert
        assertEquals(ReportStatus.IN_REVIEW, ReportStatus.valueOf("IN_REVIEW"));
        assertEquals(ReportStatus.RESOLVED, ReportStatus.valueOf("RESOLVED"));
        assertEquals(ReportStatus.DISMISSED, ReportStatus.valueOf("DISMISSED"));
    }

    @Test
    void testValueOf_WithInvalidName_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ReportStatus.valueOf("INVALID_STATUS");
        });
    }

    @Test
    void testFromString_WithValidStrings() {
        // Act & Assert
        assertEquals(ReportStatus.IN_REVIEW, ReportStatus.fromString("IN_REVIEW"));
        assertEquals(ReportStatus.RESOLVED, ReportStatus.fromString("RESOLVED"));
        assertEquals(ReportStatus.DISMISSED, ReportStatus.fromString("DISMISSED"));
    }

    @Test
    void testFromString_WithCaseInsensitiveStrings() {
        // Act & Assert
        assertEquals(ReportStatus.IN_REVIEW, ReportStatus.fromString("in_review"));
        assertEquals(ReportStatus.RESOLVED, ReportStatus.fromString("resolved"));
        assertEquals(ReportStatus.DISMISSED, ReportStatus.fromString("Dismissed"));
    }

    @Test
    void testFromString_WithNullString_ReturnsNull() {
        // Act & Assert
        assertNull(ReportStatus.fromString(null));
    }

    @Test
    void testFromString_WithInvalidString_ReturnsNull() {
        // Act & Assert
        assertNull(ReportStatus.fromString("INVALID_STATUS"));
        assertNull(ReportStatus.fromString(""));
        assertNull(ReportStatus.fromString("random"));
    }
}
