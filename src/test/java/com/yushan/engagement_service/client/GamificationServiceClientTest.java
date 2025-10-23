package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.gamification.VoteCheckResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GamificationServiceClientTest {

    @Mock
    private GamificationServiceClient gamificationServiceClient;

    private VoteCheckResponseDTO testVoteCheckResponse;
    private ApiResponse<VoteCheckResponseDTO> voteCheckResponse;

    @BeforeEach
    void setUp() {
        // Setup test vote check response
        testVoteCheckResponse = new VoteCheckResponseDTO();
        testVoteCheckResponse.setCanVote(true);
        testVoteCheckResponse.setCurrentYuanBalance(100.0);
        testVoteCheckResponse.setRequiredYuan(10.0);
        testVoteCheckResponse.setMessage("You can vote");

        // Setup response
        voteCheckResponse = ApiResponse.success("Success", testVoteCheckResponse);
    }

    @Test
    void testCheckVoteEligibility_Success() {
        // Given
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(voteCheckResponse);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(testVoteCheckResponse.isCanVote(), result.getData().isCanVote());
        assertEquals(testVoteCheckResponse.getCurrentYuanBalance(), result.getData().getCurrentYuanBalance());
        assertEquals(testVoteCheckResponse.getRequiredYuan(), result.getData().getRequiredYuan());
        assertEquals(testVoteCheckResponse.getMessage(), result.getData().getMessage());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_NullResponse() {
        // Given
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(null);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNull(result);

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_EmptyData() {
        // Given
        ApiResponse<VoteCheckResponseDTO> emptyResponse = ApiResponse.success("Success", null);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(emptyResponse);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNull(result.getData());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_ErrorResponse() {
        // Given
        ApiResponse<VoteCheckResponseDTO> errorResponse = ApiResponse.error(500, "Service unavailable");
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(errorResponse);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertEquals(500, result.getCode());
        assertEquals("Service unavailable", result.getMessage());
        assertNull(result.getData());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_CannotVote() {
        // Given
        testVoteCheckResponse.setCanVote(false);
        testVoteCheckResponse.setCurrentYuanBalance(5.0);
        testVoteCheckResponse.setRequiredYuan(10.0);
        testVoteCheckResponse.setMessage("Insufficient balance");
        ApiResponse<VoteCheckResponseDTO> response = ApiResponse.success("Success", testVoteCheckResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(response);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertFalse(result.getData().isCanVote());
        assertEquals(5.0, result.getData().getCurrentYuanBalance());
        assertEquals(10.0, result.getData().getRequiredYuan());
        assertEquals("Insufficient balance", result.getData().getMessage());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_InsufficientBalance() {
        // Given
        testVoteCheckResponse.setCanVote(false);
        testVoteCheckResponse.setCurrentYuanBalance(0.0);
        testVoteCheckResponse.setRequiredYuan(50.0);
        testVoteCheckResponse.setMessage("No balance available");
        ApiResponse<VoteCheckResponseDTO> response = ApiResponse.success("Success", testVoteCheckResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(response);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertFalse(result.getData().isCanVote());
        assertEquals(0.0, result.getData().getCurrentYuanBalance());
        assertEquals(50.0, result.getData().getRequiredYuan());
        assertEquals("No balance available", result.getData().getMessage());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_HighBalance() {
        // Given
        testVoteCheckResponse.setCanVote(true);
        testVoteCheckResponse.setCurrentYuanBalance(1000.0);
        testVoteCheckResponse.setRequiredYuan(10.0);
        testVoteCheckResponse.setMessage("You can vote multiple times");
        ApiResponse<VoteCheckResponseDTO> response = ApiResponse.success("Success", testVoteCheckResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(response);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isCanVote());
        assertEquals(1000.0, result.getData().getCurrentYuanBalance());
        assertEquals(10.0, result.getData().getRequiredYuan());
        assertEquals("You can vote multiple times", result.getData().getMessage());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_ExactBalance() {
        // Given
        testVoteCheckResponse.setCanVote(true);
        testVoteCheckResponse.setCurrentYuanBalance(10.0);
        testVoteCheckResponse.setRequiredYuan(10.0);
        testVoteCheckResponse.setMessage("Exact balance for one vote");
        ApiResponse<VoteCheckResponseDTO> response = ApiResponse.success("Success", testVoteCheckResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(response);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isCanVote());
        assertEquals(result.getData().getCurrentYuanBalance(), result.getData().getRequiredYuan());

        verify(gamificationServiceClient).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_MultipleCalls() {
        // Given
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(voteCheckResponse);

        // When
        ApiResponse<VoteCheckResponseDTO> result1 = gamificationServiceClient.checkVoteEligibility();
        ApiResponse<VoteCheckResponseDTO> result2 = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getCode(), result2.getCode());
        assertEquals(result1.getMessage(), result2.getMessage());
        assertNotNull(result1.getData());
        assertNotNull(result2.getData());
        assertEquals(result1.getData().isCanVote(), result2.getData().isCanVote());

        verify(gamificationServiceClient, times(2)).checkVoteEligibility();
    }

    @Test
    void testCheckVoteEligibility_ZeroRequiredYuan() {
        // Given
        testVoteCheckResponse.setCanVote(true);
        testVoteCheckResponse.setCurrentYuanBalance(100.0);
        testVoteCheckResponse.setRequiredYuan(0.0);
        testVoteCheckResponse.setMessage("Free vote");
        ApiResponse<VoteCheckResponseDTO> response = ApiResponse.success("Success", testVoteCheckResponse);
        when(gamificationServiceClient.checkVoteEligibility()).thenReturn(response);

        // When
        ApiResponse<VoteCheckResponseDTO> result = gamificationServiceClient.checkVoteEligibility();

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertTrue(result.getData().isCanVote());
        assertEquals(0.0, result.getData().getRequiredYuan());
        assertEquals("Free vote", result.getData().getMessage());

        verify(gamificationServiceClient).checkVoteEligibility();
    }
}
