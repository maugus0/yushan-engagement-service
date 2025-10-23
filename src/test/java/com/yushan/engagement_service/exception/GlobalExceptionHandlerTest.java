package com.yushan.engagement_service.exception;

import com.yushan.engagement_service.dto.common.ApiResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Mock
    private WebRequest webRequest;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    void testHandleAuthorizationDeniedException() {
        // Given
        AuthorizationDeniedException exception = mock(AuthorizationDeniedException.class);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleAuthorizationDeniedException(webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getCode());
        assertEquals("Access denied", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleResourceNotFoundException() {
        // Given
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleResourceNotFoundException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(404, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleValidationException() {
        // Given
        String message = "Validation failed";
        ValidationException exception = new ValidationException(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleUnauthorizedException() {
        // Given
        String message = "User not authorized";
        UnauthorizedException exception = new UnauthorizedException(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleUnauthorizedException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(401, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleForbiddenException() {
        // Given
        String message = "Access forbidden";
        ForbiddenException exception = new ForbiddenException(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleForbiddenException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(403, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleMethodArgumentNotValidException_SingleError() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError = new FieldError("testObject", "testField", "Test error message");
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(Arrays.asList(fieldError));

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Test error message", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleMethodArgumentNotValidException_MultipleErrors() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        FieldError fieldError1 = new FieldError("testObject", "field1", "Error 1");
        FieldError fieldError2 = new FieldError("testObject", "field2", "Error 2");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Error 1; Error 2", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleMethodArgumentNotValidException_NoErrors() {
        // Given
        MethodArgumentNotValidException exception = mock(MethodArgumentNotValidException.class);
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleValidationException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleBindException_SingleError() {
        // Given
        BindException exception = mock(BindException.class);
        FieldError fieldError = new FieldError("testObject", "testField", "Bind error message");
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(Arrays.asList(fieldError));

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleBindException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Bind error message", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleBindException_MultipleErrors() {
        // Given
        BindException exception = mock(BindException.class);
        FieldError fieldError1 = new FieldError("testObject", "field1", "Bind Error 1");
        FieldError fieldError2 = new FieldError("testObject", "field2", "Bind Error 2");
        List<FieldError> fieldErrors = Arrays.asList(fieldError1, fieldError2);
        
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(fieldErrors);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleBindException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("Bind Error 1; Bind Error 2", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleBindException_NoErrors() {
        // Given
        BindException exception = mock(BindException.class);
        when(exception.getBindingResult()).thenReturn(mock(org.springframework.validation.BindingResult.class));
        when(exception.getBindingResult().getFieldErrors()).thenReturn(Arrays.asList());

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleBindException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals("", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleIllegalArgumentException() {
        // Given
        String message = "Illegal argument";
        IllegalArgumentException exception = new IllegalArgumentException(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleIllegalArgumentException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(400, response.getBody().getCode());
        assertEquals(message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException() {
        // Given
        String message = "General error";
        Exception exception = new Exception(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("System error: " + message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException_NullMessage() {
        // Given
        Exception exception = new Exception();

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("System error: null", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException_RuntimeException() {
        // Given
        String message = "Runtime error";
        RuntimeException exception = new RuntimeException(message);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("System error: " + message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException_WithCause() {
        // Given
        String causeMessage = "Root cause";
        Exception cause = new Exception(causeMessage);
        String message = "Wrapper error";
        Exception exception = new Exception(message, cause);

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("System error: " + message, response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException_EmptyMessage() {
        // Given
        Exception exception = new Exception("");

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("System error: ", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void testHandleException_WebRequestUsage() {
        // Given
        Exception exception = new Exception("Test error");

        // When
        ResponseEntity<ApiResponse<Object>> response = globalExceptionHandler.handleException(exception, webRequest);

        // Then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(500, response.getBody().getCode());
        assertEquals("System error: Test error", response.getBody().getMessage());
        
        // Note: WebRequest is passed as parameter but not used in the current implementation
        // This test verifies the method works correctly with WebRequest parameter
    }
}
