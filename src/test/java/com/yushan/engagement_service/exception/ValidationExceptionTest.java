package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ValidationException
 */
class ValidationExceptionTest {

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Arrange
        String message = "Validation failed";

        // Act
        ValidationException exception = new ValidationException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_WithMessageAndCause_SetsMessageAndCause() {
        // Arrange
        String message = "Validation failed";
        Throwable cause = new IllegalArgumentException("Invalid input");

        // Act
        ValidationException exception = new ValidationException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void responseStatus_ShouldBeBadRequest() {
        // Arrange
        ValidationException exception = new ValidationException("Test");

        // Act & Assert
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.BAD_REQUEST, annotation.value());
    }

    @Test
    void inheritance_ShouldExtendRuntimeException() {
        // Arrange
        ValidationException exception = new ValidationException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
