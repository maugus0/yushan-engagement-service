package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.junit.jupiter.api.Assertions.*;

class ValidationExceptionTest {

    @Test
    void testValidationException_WithMessage() {
        // Given
        String message = "Validation failed";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
        assertEquals(HttpStatus.BAD_REQUEST.value(), 400);
    }

    @Test
    void testValidationException_WithMessageAndCause() {
        // Given
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Invalid input");

        // When
        ValidationException exception = new ValidationException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
        assertEquals(HttpStatus.BAD_REQUEST.value(), 400);
    }

    @Test
    void testValidationException_EmptyMessage() {
        // Given
        String message = "";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testValidationException_NullMessage() {
        // Given
        String message = null;

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertNotNull(exception);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testValidationException_NullCause() {
        // Given
        String message = "Validation failed";
        Throwable cause = null;

        // When
        ValidationException exception = new ValidationException(message, cause);

        // Then
        assertNotNull(exception);
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void testValidationException_Inheritance() {
        // Given
        String message = "Validation failed";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertTrue(exception instanceof RuntimeException);
        assertTrue(exception instanceof Exception);
    }

    @Test
    void testValidationException_StackTrace() {
        // Given
        String message = "Validation failed";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        assertNotNull(exception.getStackTrace());
        assertTrue(exception.getStackTrace().length > 0);
    }

    @Test
    void testValidationException_ChainedException() {
        // Given
        String message = "Validation failed";
        IllegalArgumentException cause = new IllegalArgumentException("Invalid input");
        ValidationException exception = new ValidationException(message, cause);

        // When
        RuntimeException thrown = new RuntimeException("Wrapper", exception);

        // Then
        assertNotNull(thrown.getCause());
        assertTrue(thrown.getCause() instanceof ValidationException);
        assertEquals(message, thrown.getCause().getMessage());
        assertEquals(cause, thrown.getCause().getCause());
    }

    @Test
    void testValidationException_MultipleConstructors() {
        // Given
        String message = "Validation failed";
        Throwable cause = new RuntimeException("Cause");

        // When
        ValidationException exception1 = new ValidationException(message);
        ValidationException exception2 = new ValidationException(message, cause);

        // Then
        assertNotNull(exception1);
        assertNotNull(exception2);
        assertEquals(message, exception1.getMessage());
        assertEquals(message, exception2.getMessage());
        assertNull(exception1.getCause());
        assertEquals(cause, exception2.getCause());
    }

    @Test
    void testValidationException_ToString() {
        // Given
        String message = "Validation failed";

        // When
        ValidationException exception = new ValidationException(message);

        // Then
        String toString = exception.toString();
        assertNotNull(toString);
        assertTrue(toString.contains("ValidationException"));
        assertTrue(toString.contains(message));
    }
}