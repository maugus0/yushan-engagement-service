package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.util.JwtTestUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtTestUtil jwtTestUtil;

    @BeforeEach
    void setUp() {
        when(jwtTestUtil.generateTestAuthorToken()).thenReturn("author-token");
        when(jwtTestUtil.generateTestAdminToken()).thenReturn("admin-token");
    }

    @Test
    void getAuthorToken_ShouldReturnAuthorToken() throws Exception {
        mockMvc.perform(get("/api/test/token/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("author-token"))
                .andExpect(jsonPath("$.userId").value("550e8400-e29b-41d4-a716-446655440001"))
                .andExpect(jsonPath("$.role").value("AUTHOR"))
                .andExpect(jsonPath("$.message").value("Use this token in Authorization header: Bearer <token>"));
    }

    @Test
    void getAdminToken_ShouldReturnAdminToken() throws Exception {
        mockMvc.perform(get("/api/test/token/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("admin-token"))
                .andExpect(jsonPath("$.userId").value("550e8400-e29b-41d4-a716-446655440002"))
                .andExpect(jsonPath("$.role").value("ADMIN"))
                .andExpect(jsonPath("$.message").value("Use this token in Authorization header: Bearer <token>"));
    }

    @Test
    void getAuthorToken_WithServiceException_ShouldReturnInternalServerError() throws Exception {
        when(jwtTestUtil.generateTestAuthorToken()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/test/token/author"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void getAdminToken_WithServiceException_ShouldReturnInternalServerError() throws Exception {
        when(jwtTestUtil.generateTestAdminToken()).thenThrow(new RuntimeException("Service error"));

        mockMvc.perform(get("/api/test/token/admin"))
                .andExpect(status().isInternalServerError());
    }

    @Test
    void generateTestAuthorToken_ShouldHaveCorrectHeaders() throws Exception {
        mockMvc.perform(get("/api/test/token/author"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    void generateTestAdminToken_ShouldHaveCorrectHeaders() throws Exception {
        mockMvc.perform(get("/api/test/token/admin"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    void generateTestAuthorToken_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/test/token/author"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("author-token"));
    }

    @Test
    void generateTestAdminToken_ShouldBeAccessible() throws Exception {
        mockMvc.perform(get("/api/test/token/admin"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("admin-token"));
    }
}