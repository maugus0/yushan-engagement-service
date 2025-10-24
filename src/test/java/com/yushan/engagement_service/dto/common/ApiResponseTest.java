package com.yushan.engagement_service.dto.common;

import com.yushan.engagement_service.enums.ErrorCode;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for ApiResponse class.
 * Tests all constructors, static factory methods, and getters/setters.
 */
@DisplayName("ApiResponse Tests")
class ApiResponseTest {

    @Test
    @DisplayName("Default constructor should create instance with null values")
    void testDefaultConstructor() {
        ApiResponse<String> response = new ApiResponse<>();
        
        assertEquals(0, response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getData());
        assertNull(response.getTimestamp());
    }

    @Test
    @DisplayName("AllArgsConstructor should set all fields correctly")
    void testAllArgsConstructor() {
        LocalDateTime timestamp = LocalDateTime.now();
        String data = "test data";
        
        ApiResponse<String> response = new ApiResponse<String>(200, "Success", data, timestamp);
        
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(data, response.getData());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    @DisplayName("Constructor with code, message, and data should set timestamp to now")
    void testConstructorWithCodeMessageAndData() {
        String data = "test data";
        
        ApiResponse<String> response = new ApiResponse<>(200, "Success", data);
        
        assertEquals(200, response.getCode());
        assertEquals("Success", response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("Constructor with code and message should set data to null and timestamp to now")
    void testConstructorWithCodeAndMessage() {
        ApiResponse<String> response = new ApiResponse<>(400, "Bad Request");
        
        assertEquals(400, response.getCode());
        assertEquals("Bad Request", response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
        assertTrue(response.getTimestamp().isBefore(LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    @DisplayName("success(T data) should create success response with data")
    void testSuccessWithData() {
        String data = "test data";
        
        ApiResponse<String> response = ApiResponse.success(data);
        
        assertEquals(ErrorCode.SUCCESS.getCode(), response.getCode());
        assertEquals(data, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("success(String message, T data) should create success response with custom message and data")
    void testSuccessWithMessageAndData() {
        String message = "Custom success message";
        List<String> data = Arrays.asList("item1", "item2");
        
        ApiResponse<List<String>> response = ApiResponse.success(message, data);
        
        assertEquals(ErrorCode.SUCCESS.getCode(), response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("success(String message) should create success response with custom message and null data")
    void testSuccessWithMessageOnly() {
        String message = "Operation completed successfully";
        
        ApiResponse<String> response = ApiResponse.success(message);
        
        assertEquals(ErrorCode.SUCCESS.getCode(), response.getCode());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("error(ErrorCode) should create error response with error code")
    void testErrorWithErrorCode() {
        ApiResponse<String> response = ApiResponse.error(ErrorCode.NOT_FOUND);
        
        assertEquals(ErrorCode.NOT_FOUND.getCode(), response.getCode());
        assertEquals(ErrorCode.NOT_FOUND.getMessage(), response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("error(ErrorCode, String message) should create error response with custom message")
    void testErrorWithErrorCodeAndMessage() {
        String customMessage = "Resource not found in database";
        
        ApiResponse<String> response = ApiResponse.error(ErrorCode.NOT_FOUND, customMessage);
        
        assertEquals(ErrorCode.NOT_FOUND.getCode(), response.getCode());
        assertEquals(customMessage, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("error(ErrorCode, String message, T data) should create error response with custom message and data")
    void testErrorWithErrorCodeMessageAndData() {
        String customMessage = "Validation failed";
        List<String> errors = Arrays.asList("Field1 is required", "Field2 is invalid");
        
        ApiResponse<List<String>> response = ApiResponse.error(ErrorCode.BAD_REQUEST, customMessage, errors);
        
        assertEquals(ErrorCode.BAD_REQUEST.getCode(), response.getCode());
        assertEquals(customMessage, response.getMessage());
        assertEquals(errors, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("error(int code, String message) should create error response with custom code and message")
    void testErrorWithCustomCodeAndMessage() {
        int customCode = 422;
        String message = "Unprocessable Entity";
        
        ApiResponse<String> response = ApiResponse.error(customCode, message);
        
        assertEquals(customCode, response.getCode());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("error(int code, String message, T data) should create error response with custom code, message and data")
    void testErrorWithCustomCodeMessageAndData() {
        int customCode = 409;
        String message = "Conflict occurred";
        String conflictData = "Resource already exists";
        
        ApiResponse<String> response = ApiResponse.error(customCode, message, conflictData);
        
        assertEquals(customCode, response.getCode());
        assertEquals(message, response.getMessage());
        assertEquals(conflictData, response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Setters should update fields correctly")
    void testSetters() {
        ApiResponse<String> response = new ApiResponse<>();
        LocalDateTime timestamp = LocalDateTime.now();
        String data = "updated data";
        
        response.setCode(201);
        response.setMessage("Created");
        response.setData(data);
        response.setTimestamp(timestamp);
        
        assertEquals(201, response.getCode());
        assertEquals("Created", response.getMessage());
        assertEquals(data, response.getData());
        assertEquals(timestamp, response.getTimestamp());
    }

    @Test
    @DisplayName("ApiResponse should work with different generic types")
    void testGenericTypes() {
        // Test with Integer
        ApiResponse<Integer> intResponse = ApiResponse.success(42);
        assertEquals(Integer.valueOf(42), intResponse.getData());
        
        // Test with List
        List<String> listData = Arrays.asList("a", "b", "c");
        ApiResponse<List<String>> listResponse = ApiResponse.success(listData);
        assertEquals(listData, listResponse.getData());
        
        // Test with null data
        ApiResponse<Object> nullResponse = ApiResponse.success("message");
        assertNull(nullResponse.getData());
    }

    @Test
    @DisplayName("ApiResponse should handle null data correctly")
    void testNullDataHandling() {
        ApiResponse<String> response = ApiResponse.success((String) null);
        
        assertEquals(ErrorCode.SUCCESS.getCode(), response.getCode());
        assertNull(response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("ApiResponse should handle empty string data correctly")
    void testEmptyStringData() {
        ApiResponse<String> response = ApiResponse.success("");
        
        assertEquals(ErrorCode.SUCCESS.getCode(), response.getCode());
        assertEquals("", response.getMessage());
        assertNull(response.getData());
        assertNotNull(response.getTimestamp());
    }

    @Test
    @DisplayName("Multiple static factory methods should create independent instances")
    void testStaticFactoryMethodIndependence() {
        ApiResponse<String> response1 = ApiResponse.success("data1");
        ApiResponse<String> response2 = ApiResponse.success("data2");
        
        assertNotSame(response1, response2);
        assertNotEquals(response1.getTimestamp(), response2.getTimestamp());
        assertEquals("data1", response1.getMessage());
        assertEquals("data2", response2.getMessage());
    }
}
