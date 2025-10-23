package com.yushan.engagement_service.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ReportType enum
 */
class ReportTypeTest {

    @Test
    void testPornographic() {
        // Act & Assert
        assertEquals("Pornographic Content", ReportType.PORNOGRAPHIC.getDescription());
    }

    @Test
    void testHateBullying() {
        // Act & Assert
        assertEquals("Hate or Bullying", ReportType.HATE_BULLYING.getDescription());
    }

    @Test
    void testPersonalInfo() {
        // Act & Assert
        assertEquals("Release of personal info", ReportType.PERSONAL_INFO.getDescription());
    }

    @Test
    void testInappropriate() {
        // Act & Assert
        assertEquals("Other inappropriate material", ReportType.INAPPROPRIATE.getDescription());
    }

    @Test
    void testSpam() {
        // Act & Assert
        assertEquals("Spam", ReportType.SPAM.getDescription());
    }

    @Test
    void testEnumValues() {
        // Act
        ReportType[] values = ReportType.values();

        // Assert
        assertEquals(5, values.length);
        assertEquals(ReportType.PORNOGRAPHIC, values[0]);
        assertEquals(ReportType.HATE_BULLYING, values[1]);
        assertEquals(ReportType.PERSONAL_INFO, values[2]);
        assertEquals(ReportType.INAPPROPRIATE, values[3]);
        assertEquals(ReportType.SPAM, values[4]);
    }

    @Test
    void testValueOf() {
        // Act & Assert
        assertEquals(ReportType.PORNOGRAPHIC, ReportType.valueOf("PORNOGRAPHIC"));
        assertEquals(ReportType.HATE_BULLYING, ReportType.valueOf("HATE_BULLYING"));
        assertEquals(ReportType.PERSONAL_INFO, ReportType.valueOf("PERSONAL_INFO"));
        assertEquals(ReportType.INAPPROPRIATE, ReportType.valueOf("INAPPROPRIATE"));
        assertEquals(ReportType.SPAM, ReportType.valueOf("SPAM"));
    }

    @Test
    void testValueOf_WithInvalidName_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ReportType.valueOf("INVALID_TYPE");
        });
    }

    @Test
    void testFromString_WithValidStrings() {
        // Act & Assert
        assertEquals(ReportType.PORNOGRAPHIC, ReportType.fromString("PORNOGRAPHIC"));
        assertEquals(ReportType.HATE_BULLYING, ReportType.fromString("HATE_BULLYING"));
        assertEquals(ReportType.PERSONAL_INFO, ReportType.fromString("PERSONAL_INFO"));
        assertEquals(ReportType.INAPPROPRIATE, ReportType.fromString("INAPPROPRIATE"));
        assertEquals(ReportType.SPAM, ReportType.fromString("SPAM"));
    }

    @Test
    void testFromString_WithCaseInsensitiveStrings() {
        // Act & Assert
        assertEquals(ReportType.PORNOGRAPHIC, ReportType.fromString("pornographic"));
        assertEquals(ReportType.HATE_BULLYING, ReportType.fromString("hate_bullying"));
        assertEquals(ReportType.PERSONAL_INFO, ReportType.fromString("Personal_Info"));
        assertEquals(ReportType.INAPPROPRIATE, ReportType.fromString("inappropriate"));
        assertEquals(ReportType.SPAM, ReportType.fromString("spam"));
    }

    @Test
    void testFromString_WithNullString_ReturnsNull() {
        // Act & Assert
        assertNull(ReportType.fromString(null));
    }

    @Test
    void testFromString_WithInvalidString_ReturnsNull() {
        // Act & Assert
        assertNull(ReportType.fromString("INVALID_TYPE"));
        assertNull(ReportType.fromString(""));
        assertNull(ReportType.fromString("random"));
    }
}
