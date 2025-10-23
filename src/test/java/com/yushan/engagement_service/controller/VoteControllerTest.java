package com.yushan.engagement_service.controller;

import com.yushan.engagement_service.dto.vote.VoteResponseDTO;
import com.yushan.engagement_service.dto.vote.VoteUserResponseDTO;
import com.yushan.engagement_service.dto.common.PageResponseDTO;
import com.yushan.engagement_service.security.CustomUserDetails;
import com.yushan.engagement_service.service.VoteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.List;
import java.util.ArrayList;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class VoteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VoteService voteService;

    @MockBean
    private Authentication authentication;

    private CustomUserDetails customUserDetails;

    @BeforeEach
    void setUp() {
        // Setup SecurityContext with CustomUserDetails
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        customUserDetails = new CustomUserDetails(
            testUserId.toString(),
            "test@example.com",
            "testuser",
            "USER",
            0
        );
        
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
            customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    @Test
    void createVote_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Integer testNovelId = 1;
        
        VoteResponseDTO mockResponse = new VoteResponseDTO();
        mockResponse.setNovelId(testNovelId);
        mockResponse.setIsVoted(true);
        
        when(voteService.createVote(eq(testNovelId), eq(testUserId))).thenReturn(mockResponse);
        
        // Execute & Verify
        mockMvc.perform(post("/api/v1/votes/novels/{novelId}", testNovelId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("Vote created successfully"))
                .andExpect(jsonPath("$.data.novelId").value(testNovelId))
                .andExpect(jsonPath("$.data.isVoted").value(true));
    }

    @Test
    void getUserVotes_WithValidData_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        int page = 0;
        int size = 20;
        
        VoteUserResponseDTO mockVote = new VoteUserResponseDTO();
        mockVote.setNovelId(1);
        mockVote.setNovelTitle("Test Novel");
        
        List<VoteUserResponseDTO> mockVotes = new ArrayList<>();
        mockVotes.add(mockVote);
        
        PageResponseDTO<VoteUserResponseDTO> mockPageResponse = new PageResponseDTO<>();
        mockPageResponse.setContent(mockVotes);
        mockPageResponse.setCurrentPage(page);
        mockPageResponse.setSize(size);
        mockPageResponse.setTotalElements(1L);
        mockPageResponse.setTotalPages(1);
        
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(voteService.getUserVotes(eq(testUserId), eq(page), eq(size))).thenReturn(mockPageResponse);
        
        // Use @Autowired MockMvc instead

        // Execute & Verify
        mockMvc.perform(get("/api/v1/votes/users")
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.message").value("User votes retrieved"))
                .andExpect(jsonPath("$.data.content[0].novelId").value(1))
                .andExpect(jsonPath("$.data.content[0].novelTitle").value("Test Novel"))
                .andExpect(jsonPath("$.data.currentPage").value(page))
                .andExpect(jsonPath("$.data.size").value(size));
    }

    @Test
    void createVote_WithDefaultParameters_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Integer testNovelId = 2;
        
        VoteResponseDTO mockResponse = new VoteResponseDTO();
        mockResponse.setNovelId(testNovelId);
        mockResponse.setIsVoted(false);
        
        when(voteService.createVote(eq(testNovelId), eq(testUserId))).thenReturn(mockResponse);
        
        // Execute & Verify
        mockMvc.perform(post("/api/v1/votes/novels/{novelId}", testNovelId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.novelId").value(testNovelId))
                .andExpect(jsonPath("$.data.isVoted").value(false));
    }

    @Test
    void getUserVotes_WithDefaultParameters_ShouldReturnSuccess() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        PageResponseDTO<VoteUserResponseDTO> mockPageResponse = new PageResponseDTO<>();
        mockPageResponse.setContent(new ArrayList<>());
        mockPageResponse.setCurrentPage(0);
        mockPageResponse.setSize(20);
        mockPageResponse.setTotalElements(0L);
        mockPageResponse.setTotalPages(0);
        
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(voteService.getUserVotes(eq(testUserId), eq(0), eq(20))).thenReturn(mockPageResponse);
        
        // Use @Autowired MockMvc instead

        // Execute & Verify
        mockMvc.perform(get("/api/v1/votes/users")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data.content").isEmpty())
                .andExpect(jsonPath("$.data.currentPage").value(0))
                .andExpect(jsonPath("$.data.size").value(20));
    }

    @Test
    void createVote_ShouldHaveCorrectHeaders() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        Integer testNovelId = 1;
        
        VoteResponseDTO mockResponse = new VoteResponseDTO();
        mockResponse.setNovelId(testNovelId);
        mockResponse.setIsVoted(true);
        
        when(voteService.createVote(eq(testNovelId), eq(testUserId))).thenReturn(mockResponse);
        
        // Execute & Verify
        mockMvc.perform(post("/api/v1/votes/novels/{novelId}", testNovelId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }

    @Test
    void getUserVotes_ShouldHaveCorrectHeaders() throws Exception {
        // Setup
        UUID testUserId = UUID.fromString("550e8400-e29b-41d4-a716-446655440001");
        
        PageResponseDTO<VoteUserResponseDTO> mockPageResponse = new PageResponseDTO<>();
        mockPageResponse.setContent(new ArrayList<>());
        mockPageResponse.setCurrentPage(0);
        mockPageResponse.setSize(20);
        mockPageResponse.setTotalElements(0L);
        mockPageResponse.setTotalPages(0);
        
        when(authentication.getPrincipal()).thenReturn(customUserDetails);
        when(voteService.getUserVotes(eq(testUserId), eq(0), eq(20))).thenReturn(mockPageResponse);
        
        // Use @Autowired MockMvc instead

        // Execute & Verify
        mockMvc.perform(get("/api/v1/votes/users")
                .principal(authentication))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Type", "application/json"));
    }
}
