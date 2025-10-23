package com.yushan.engagement_service.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ErrorCode enum
 */
class ErrorCodeTest {

    @Test
    void testSuccess() {
        // Act & Assert
        assertEquals(200, ErrorCode.SUCCESS.getCode());
        assertEquals("Success", ErrorCode.SUCCESS.getMessage());
    }

    @Test
    void testBadRequest() {
        // Act & Assert
        assertEquals(400, ErrorCode.BAD_REQUEST.getCode());
        assertEquals("Bad Request", ErrorCode.BAD_REQUEST.getMessage());
    }

    @Test
    void testUnauthorized() {
        // Act & Assert
        assertEquals(401, ErrorCode.UNAUTHORIZED.getCode());
        assertEquals("Unauthorized", ErrorCode.UNAUTHORIZED.getMessage());
    }

    @Test
    void testForbidden() {
        // Act & Assert
        assertEquals(403, ErrorCode.FORBIDDEN.getCode());
        assertEquals("Forbidden", ErrorCode.FORBIDDEN.getMessage());
    }

    @Test
    void testNotFound() {
        // Act & Assert
        assertEquals(404, ErrorCode.NOT_FOUND.getCode());
        assertEquals("Not Found", ErrorCode.NOT_FOUND.getMessage());
    }

    @Test
    void testInternalServerError() {
        // Act & Assert
        assertEquals(500, ErrorCode.INTERNAL_SERVER_ERROR.getCode());
        assertEquals("Internal Server Error", ErrorCode.INTERNAL_SERVER_ERROR.getMessage());
    }

    @Test
    void testEnumValues() {
        // Act
        ErrorCode[] values = ErrorCode.values();

        // Assert
        assertEquals(6, values.length);
        assertEquals(ErrorCode.SUCCESS, values[0]);
        assertEquals(ErrorCode.BAD_REQUEST, values[1]);
        assertEquals(ErrorCode.UNAUTHORIZED, values[2]);
        assertEquals(ErrorCode.FORBIDDEN, values[3]);
        assertEquals(ErrorCode.NOT_FOUND, values[4]);
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, values[5]);
    }

    @Test
    void testValueOf() {
        // Act & Assert
        assertEquals(ErrorCode.SUCCESS, ErrorCode.valueOf("SUCCESS"));
        assertEquals(ErrorCode.BAD_REQUEST, ErrorCode.valueOf("BAD_REQUEST"));
        assertEquals(ErrorCode.UNAUTHORIZED, ErrorCode.valueOf("UNAUTHORIZED"));
        assertEquals(ErrorCode.FORBIDDEN, ErrorCode.valueOf("FORBIDDEN"));
        assertEquals(ErrorCode.NOT_FOUND, ErrorCode.valueOf("NOT_FOUND"));
        assertEquals(ErrorCode.INTERNAL_SERVER_ERROR, ErrorCode.valueOf("INTERNAL_SERVER_ERROR"));
    }

    @Test
    void testValueOf_WithInvalidName_ThrowsException() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            ErrorCode.valueOf("INVALID_ERROR_CODE");
        });
    }
}
