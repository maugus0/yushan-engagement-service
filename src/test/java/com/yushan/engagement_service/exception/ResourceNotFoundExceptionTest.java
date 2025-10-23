package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ResourceNotFoundException
 */
class ResourceNotFoundExceptionTest {

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Arrange
        String message = "Resource not found";

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_WithMessageAndCause_SetsMessageAndCause() {
        // Arrange
        String message = "Resource not found";
        Throwable cause = new IllegalArgumentException("Invalid ID");

        // Act
        ResourceNotFoundException exception = new ResourceNotFoundException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void responseStatus_ShouldBeNotFound() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        // Act & Assert
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.NOT_FOUND, annotation.value());
    }

    @Test
    void inheritance_ShouldExtendRuntimeException() {
        // Arrange
        ResourceNotFoundException exception = new ResourceNotFoundException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
