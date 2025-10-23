package com.yushan.engagement_service.exception;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UnauthorizedException
 */
class UnauthorizedExceptionTest {

    @Test
    void constructor_WithMessage_SetsMessage() {
        // Arrange
        String message = "Unauthorized access";

        // Act
        UnauthorizedException exception = new UnauthorizedException(message);

        // Assert
        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void constructor_WithMessageAndCause_SetsMessageAndCause() {
        // Arrange
        String message = "Unauthorized access";
        Throwable cause = new SecurityException("Invalid token");

        // Act
        UnauthorizedException exception = new UnauthorizedException(message, cause);

        // Assert
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void responseStatus_ShouldBeUnauthorized() {
        // Arrange
        UnauthorizedException exception = new UnauthorizedException("Test");

        // Act & Assert
        ResponseStatus annotation = exception.getClass().getAnnotation(ResponseStatus.class);
        assertNotNull(annotation);
        assertEquals(HttpStatus.UNAUTHORIZED, annotation.value());
    }

    @Test
    void inheritance_ShouldExtendRuntimeException() {
        // Arrange
        UnauthorizedException exception = new UnauthorizedException("Test");

        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}
