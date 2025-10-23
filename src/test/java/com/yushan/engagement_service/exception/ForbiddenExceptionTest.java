package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ForbiddenExceptionTest {

    @Test
    void testForbiddenException_WithMessage() {
        // Given
        String message = "Access forbidden";

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(HttpStatus.FORBIDDEN.value(), 403);
    }

    @Test
    void testForbiddenException_WithMessageAndCause() {
        // Given
        String message = "Access forbidden";
        Throwable cause = new SecurityException("Insufficient permissions");

        // When
        ForbiddenException exception = new ForbiddenException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(HttpStatus.FORBIDDEN.value(), 403);
    }

    @Test
    void testForbiddenException_EmptyMessage() {
        // Given
        String message = "";

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testForbiddenException_NullMessage() {
        // Given
        String message = null;

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testForbiddenException_NullCause() {
        // Given
        String message = "Access forbidden";
        Throwable cause = null;

        // When
        ForbiddenException exception = new ForbiddenException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testForbiddenException_Inheritance() {
        // Given
        String message = "Access forbidden";

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testForbiddenException_StackTrace() {
        // Given
        String message = "Access forbidden";

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testForbiddenException_ChainedException() {
        // Given
        String message = "Access forbidden";
        SecurityException cause = new SecurityException("Insufficient permissions");
        ForbiddenException exception = new ForbiddenException(message, cause);

        // When
        RuntimeException thrown = new RuntimeException("Wrapper", exception);

        // Then
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof ForbiddenException);
        assertEquals(message, thrown.getCause().getMessage());
        assertEquals(cause, thrown.getCause().getCause());
    }

    @Test
    void testForbiddenException_MultipleConstructors() {
        // Given
        String message = "Access forbidden";
        Throwable cause = new RuntimeException("Cause");

        // When
        ForbiddenException exception1 = new ForbiddenException(message);
        ForbiddenException exception2 = new ForbiddenException(message, cause);

        // Then
        assertNotNull(exception1);
        assertNotNull(exception2);
        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertNull(exception1.getCause());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testForbiddenException_ToString() {
        // Given
        String message = "Access forbidden";

        // When
        ForbiddenException exception = new ForbiddenException(message);

        // Then
        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ForbiddenException"));
        assertTrue(toString.contains(message));
    }
}