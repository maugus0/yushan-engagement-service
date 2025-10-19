package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.util.JwtTestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Test Controller for generating JWT tokens
 * This is only for development/testing purposes
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JwtTestUtil jwtTestUtil;

    private static final UUID TEST_USER_ID = UUID.fromString("45d64366-e0f1-4772-b013-287e3059efc3");
    private static final UUID TEST_AUTHOR_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
    private static final UUID TEST_ADMIN_ID = UUID.fromString("660e8400-e29b-41d4-a716-446655440002");
    private static final UUID TEST_SUSPENDED_ID = UUID.fromString("770e8400-e29b-41d4-a716-446655440003");

    /**
     * Generate test JWT token for USER role
     */
    @GetMapping("/token/user")
    public Map<String, String> getUserToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTestUtil.generateAccessToken(TEST_USER_ID, "testuser@yushan.com", "USER", 0));
        response.put("userId", TEST_USER_ID.toString());
        response.put("role", "USER");
        response.put("message", "Use this token in Authorization header: Bearer <token>");
        return response;
    }

    /**
     * Generate test JWT token for AUTHOR role
     */
    @GetMapping("/token/author")
    public Map<String, String> getAuthorToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTestUtil.generateAccessToken(TEST_AUTHOR_ID, "testauthor@yushan.com", "AUTHOR", 0));
        response.put("userId", TEST_AUTHOR_ID.toString());
        response.put("role", "AUTHOR");
        response.put("message", "Use this token in Authorization header: Bearer <token>");
        return response;
    }

    /**
     * Generate test JWT token for ADMIN role
     */
    @GetMapping("/token/admin")
    public Map<String, String> getAdminToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTestUtil.generateAccessToken(TEST_ADMIN_ID, "testadmin@yushan.com", "ADMIN", 0));
        response.put("userId", TEST_ADMIN_ID.toString());
        response.put("role", "ADMIN");
        response.put("message", "Use this token in Authorization header: Bearer <token>");
        return response;
    }

    /**
     * Generate test JWT token for suspended user
     */
    @GetMapping("/token/suspended")
    public Map<String, String> getSuspendedToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTestUtil.generateAccessToken(TEST_SUSPENDED_ID, "suspended@yushan.com", "USER", 1)); // Status 1 for suspended
        response.put("userId", TEST_SUSPENDED_ID.toString());
        response.put("role", "USER (SUSPENDED)");
        response.put("message", "This token should be rejected due to suspended status");
        return response;
    }

    /**
     * Generate expired token for testing
     */
    @GetMapping("/token/expired")
    public Map<String, String> getExpiredToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTestUtil.generateExpiredToken(TEST_USER_ID, "expired@yushan.com", "USER"));
        response.put("userId", TEST_USER_ID.toString());
        response.put("role", "USER (EXPIRED)");
        response.put("message", "This token should be rejected due to expiration");
        return response;
    }
}
