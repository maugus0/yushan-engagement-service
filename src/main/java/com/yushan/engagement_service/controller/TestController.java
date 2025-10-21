package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.util.JwtTestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Test Controller for generating JWT tokens
 * This is only for development/testing purposes
 */
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired
    private JwtTestUtil jwtTestUtil;

    /**
     * Generate test JWT token for AUTHOR role
     */
    @GetMapping("/token/author")
    public Map<String, String> getAuthorToken() {
        Map<String, String> response = new HashMap<>();
        response.put("token", jwtTestUtil.generateTestAuthorToken());
        response.put("userId", "550e8400-e29b-41d4-a716-446655440001");
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
        response.put("token", jwtTestUtil.generateTestAdminToken());
        response.put("userId", "550e8400-e29b-41d4-a716-446655440002");
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
        response.put("token", jwtTestUtil.generateTestSuspendedToken());
        response.put("userId", "550e8400-e29b-41d4-a716-446655440003");
        response.put("role", "AUTHOR (SUSPENDED)");
        response.put("message", "This token should be rejected due to suspended status");
        return response;
    }
}
