package com.yushan.engagement_service.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserStatus enum
 */
class UserStatusTest {

    @Test
    void testNormal() {
        // Act & Assert
        assertEquals(0, UserStatus.NORMAL.getCode());
        assertEquals("NORMAL", UserStatus.NORMAL.toString());
    }

    @Test
    void testSuspended() {
        // Act & Assert
        assertEquals(1, UserStatus.SUSPENDED.getCode());
        assertEquals("SUSPENDED", UserStatus.SUSPENDED.toString());
    }

    @Test
    void testBanned() {
        // Act & Assert
        assertEquals(2, UserStatus.BANNED.getCode());
        assertEquals("BANNED", UserStatus.BANNED.toString());
    }

    @Test
    void testEnumValues() {
        // Act
        UserStatus[] values = UserStatus.values();

        // Assert
        assertEquals(3, values.length);
        assertEquals(UserStatus.NORMAL, values[0]);
        assertEquals(UserStatus.SUSPENDED, values[1]);
        assertEquals(UserStatus.BANNED, values[2]);
    }

    @Test
    void testValueOf() {
        // Act & Assert
        assertEquals(UserStatus.NORMAL, UserStatus.valueOf("NORMAL"));
        assertEquals(UserStatus.SUSPENDED, UserStatus.valueOf("SUSPENDED"));
        assertEquals(UserStatus.BANNED, UserStatus.valueOf("BANNED"));
    }

    @Test
    void testValueOf_WithInvalidName_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            UserStatus.valueOf("INVALID_STATUS");
        });
    }

    @Test
    void testFromCode_WithValidCodes() {
        // Act & Assert
        assertEquals(UserStatus.NORMAL, UserStatus.fromCode(0));
        assertEquals(UserStatus.SUSPENDED, UserStatus.fromCode(1));
        assertEquals(UserStatus.BANNED, UserStatus.fromCode(2));
    }

    @Test
    void testFromCode_WithNullCode_ReturnsNull() {
        // Act & Assert
        assertNull(UserStatus.fromCode(null));
    }

    @Test
    void testFromCode_WithInvalidCode_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            UserStatus.fromCode(999);
        });
    }

    @Test
    void testToString_ReturnsName() {
        // Act & Assert
        assertEquals("NORMAL", UserStatus.NORMAL.toString());
        assertEquals("SUSPENDED", UserStatus.SUSPENDED.toString());
        assertEquals("BANNED", UserStatus.BANNED.toString());
    }
}
