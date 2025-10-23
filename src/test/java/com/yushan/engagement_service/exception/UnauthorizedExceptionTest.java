package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class UnauthorizedExceptionTest {

    @Test
    void testUnauthorizedException_WithMessage() {
        // Given
        String message = "User not authorized";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), 401);
    }

    @Test
    void testUnauthorizedException_WithMessageAndCause() {
        // Given
        String message = "User not authorized";
        Throwable cause = new SecurityException("Invalid token");

        // When
        UnauthorizedException exception = new UnauthorizedException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), 401);
    }

    @Test
    void testUnauthorizedException_EmptyMessage() {
        // Given
        String message = "";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testUnauthorizedException_NullMessage() {
        // Given
        String message = null;

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testUnauthorizedException_NullCause() {
        // Given
        String message = "User not authorized";
        Throwable cause = null;

        // When
        UnauthorizedException exception = new UnauthorizedException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testUnauthorizedException_Inheritance() {
        // Given
        String message = "User not authorized";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testUnauthorizedException_StackTrace() {
        // Given
        String message = "User not authorized";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testUnauthorizedException_ChainedException() {
        // Given
        String message = "User not authorized";
        SecurityException cause = new SecurityException("Invalid token");
        UnauthorizedException exception = new UnauthorizedException(message, cause);

        // When
        RuntimeException thrown = new RuntimeException("Wrapper", exception);

        // Then
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof UnauthorizedException);
        assertEquals(message, thrown.getCause().getMessage());
        assertEquals(cause, thrown.getCause().getCause());
    }

    @Test
    void testUnauthorizedException_MultipleConstructors() {
        // Given
        String message = "User not authorized";
        Throwable cause = new RuntimeException("Cause");

        // When
        UnauthorizedException exception1 = new UnauthorizedException(message);
        UnauthorizedException exception2 = new UnauthorizedException(message, cause);

        // Then
        assertNotNull(exception1);
        assertNotNull(exception2);
        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertNull(exception1.getCause());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testUnauthorizedException_ToString() {
        // Given
        String message = "User not authorized";

        // When
        UnauthorizedException exception = new UnauthorizedException(message);

        // Then
        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("UnauthorizedException"));
        assertTrue(toString.contains(message));
    }
}