package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ForbiddenException
 */
class ForbiddenExceptionTest {

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Arrange
        String message = "Access forbidden";

        // Act
        ForbiddenException exception = new ForbiddenException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_WithMessageAndCause_SetsMessageAndCause() {
        // Arrange
        String message = "Access forbidden";
        Throwable cause = new SecurityException("Insufficient permissions");

        // Act
        ForbiddenException exception = new ForbiddenException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void responseStatus_ShouldBeForbidden() {
        // Arrange
        ForbiddenException exception = new ForbiddenException("Test");

        // Act & Assert
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.FORBIDDEN, annotation.value());
    }

    @Test
    void inheritance_ShouldExtendRuntimeException() {
        // Arrange
        ForbiddenException exception = new ForbiddenException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
