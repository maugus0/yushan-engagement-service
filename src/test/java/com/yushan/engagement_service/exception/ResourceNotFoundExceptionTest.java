package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundException_WithMessage() {
        // Given
        String message = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(HttpStatus.NOT_FOUND.value(), 404);
    }

    @Test
    void testResourceNotFoundException_WithMessageAndCause() {
        // Given
        String message = "Resource not found";
        Throwable cause = new IllegalArgumentException("Invalid ID");

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(HttpStatus.NOT_FOUND.value(), 404);
    }

    @Test
    void testResourceNotFoundException_EmptyMessage() {
        // Given
        String message = "";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testResourceNotFoundException_NullMessage() {
        // Given
        String message = null;

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testResourceNotFoundException_NullCause() {
        // Given
        String message = "Resource not found";
        Throwable cause = null;

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testResourceNotFoundException_Inheritance() {
        // Given
        String message = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testResourceNotFoundException_StackTrace() {
        // Given
        String message = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testResourceNotFoundException_ChainedException() {
        // Given
        String message = "Resource not found";
        IllegalArgumentException cause = new IllegalArgumentException("Invalid ID");
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

        // When
        RuntimeException thrown = new RuntimeException("Wrapper", exception);

        // Then
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof ResourceNotFoundException);
        assertEquals(message, thrown.getCause().getMessage());
        assertEquals(cause, thrown.getCause().getCause());
    }

    @Test
    void testResourceNotFoundException_MultipleConstructors() {
        // Given
        String message = "Resource not found";
        Throwable cause = new RuntimeException("Cause");

        // When
        ResourceNotFoundException exception1 = new ResourceNotFoundException(message);
        ResourceNotFoundException exception2 = new ResourceNotFoundException(message, cause);

        // Then
        assertNotNull(exception1);
        assertNotNull(exception2);
        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertNull(exception1.getCause());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testResourceNotFoundException_ToString() {
        // Given
        String message = "Resource not found";

        // When
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Then
        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ResourceNotFoundException"));
        assertTrue(toString.contains(message));
    }
}