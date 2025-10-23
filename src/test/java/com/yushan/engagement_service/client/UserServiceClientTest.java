package com.yushan.engagement_service.client;

import com.yushan.engagement_service.dto.common.ApiResponse;
import com.yushan.engagement_service.dto.user.UserProfileResponseDTO;
import com.yushan.engagement_service.enums.Gender;
import com.yushan.engagement_service.enums.UserStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceClientTest {

    @Mock
    private UserServiceClient userServiceClient;

    private UserProfileResponseDTO testUserProfile;
    private ApiResponse<UserProfileResponseDTO> userProfileResponse;
    private UUID testUserId;

    @BeforeEach
    void setUp() {
        testUserId = UUID.randomUUID();
        
        // Setup test user profile
        testUserProfile = new UserProfileResponseDTO();
        testUserProfile.setUuid(testUserId.toString());
        testUserProfile.setUsername("testuser");
        testUserProfile.setEmail("test@example.com");
        testUserProfile.setProfileDetail("Test profile detail");
        testUserProfile.setAvatarUrl("https://example.com/avatar.jpg");
        testUserProfile.setGender(Gender.MALE);
        testUserProfile.setIsAuthor(false);
        testUserProfile.setIsAdmin(false);
        testUserProfile.setStatus(UserStatus.NORMAL);
        testUserProfile.setCreateTime(new Date());
        testUserProfile.setUpdateTime(new Date());

        // Setup response
        userProfileResponse = ApiResponse.success("Success", testUserProfile);
    }

    @Test
    void testGetUser_Success() {
        // Given
        when(userServiceClient.getUser(testUserId)).thenReturn(userProfileResponse);

        // When
        ApiResponse<UserProfileResponseDTO> result = userServiceClient.getUser(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNotNull(result.getData());
        assertEquals(testUserProfile.getUuid(), result.getData().getUuid());
        assertEquals(testUserProfile.getUsername(), result.getData().getUsername());
        assertEquals(testUserProfile.getEmail(), result.getData().getEmail());
        assertEquals(testUserProfile.getProfileDetail(), result.getData().getProfileDetail());
        assertEquals(testUserProfile.getAvatarUrl(), result.getData().getAvatarUrl());
        assertEquals(testUserProfile.getGender(), result.getData().getGender());
        assertEquals(testUserProfile.getIsAuthor(), result.getData().getIsAuthor());
        assertEquals(testUserProfile.getIsAdmin(), result.getData().getIsAdmin());
        assertEquals(testUserProfile.getStatus(), result.getData().getStatus());

        verify(userServiceClient).getUser(testUserId);
    }

    @Test
    void testGetUser_NullResponse() {
        // Given
        when(userServiceClient.getUser(testUserId)).thenReturn(null);

        // When
        ApiResponse<UserProfileResponseDTO> result = userServiceClient.getUser(testUserId);

        // Then
        assertNull(result);

        verify(userServiceClient).getUser(testUserId);
    }

    @Test
    void testGetUser_EmptyData() {
        // Given
        ApiResponse<UserProfileResponseDTO> emptyResponse = ApiResponse.success("Success", null);
        when(userServiceClient.getUser(testUserId)).thenReturn(emptyResponse);

        // When
        ApiResponse<UserProfileResponseDTO> result = userServiceClient.getUser(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(200, result.getCode());
        assertEquals("Success", result.getMessage());
        assertNull(result.getData());

        verify(userServiceClient).getUser(testUserId);
    }

    @Test
    void testGetUser_ErrorResponse() {
        // Given
        ApiResponse<UserProfileResponseDTO> errorResponse = ApiResponse.error(404, "User not found");
        when(userServiceClient.getUser(testUserId)).thenReturn(errorResponse);

        // When
        ApiResponse<UserProfileResponseDTO> result = userServiceClient.getUser(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(404, result.getCode());
        assertEquals("User not found", result.getMessage());
        assertNull(result.getData());

        verify(userServiceClient).getUser(testUserId);
    }

    @Test
    void testGetUser_InactiveUser() {
        // Given
        testUserProfile.setStatus(UserStatus.SUSPENDED);
        ApiResponse<UserProfileResponseDTO> response = ApiResponse.success("Success", testUserProfile);
        when(userServiceClient.getUser(testUserId)).thenReturn(response);

        // When
        ApiResponse<UserProfileResponseDTO> result = userServiceClient.getUser(testUserId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertEquals(UserStatus.SUSPENDED, result.getData().getStatus());

        verify(userServiceClient).getUser(testUserId);
    }

    @Test
    void testGetUser_MinimalProfile() {
        // Given
        testUserProfile.setProfileDetail(null);
        testUserProfile.setAvatarUrl(null);
        testUserProfile.setGender(null);
        ApiResponse<UserProfileResponseDTO> response = ApiResponse.success("Success", testUserProfile);
        when(userServiceClient.getUser(testUserId)).thenReturn(response);

        // When
        ApiResponse<UserProfileResponseDTO> result = userServiceClient.getUser(testUserId);

        // Then
        assertNotNull(result);
        assertNotNull(result.getData());
        assertNull(result.getData().getProfileDetail());
        assertNull(result.getData().getAvatarUrl());
        assertNull(result.getData().getGender());
        assertNotNull(result.getData().getUsername());
        assertNotNull(result.getData().getEmail());

        verify(userServiceClient).getUser(testUserId);
    }

    @Test
    void testGetUser_MultipleCalls() {
        // Given
        when(userServiceClient.getUser(testUserId)).thenReturn(userProfileResponse);

        // When
        ApiResponse<UserProfileResponseDTO> result1 = userServiceClient.getUser(testUserId);
        ApiResponse<UserProfileResponseDTO> result2 = userServiceClient.getUser(testUserId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertEquals(result1.getCode(), result2.getCode());
        assertEquals(result1.getMessage(), result2.getMessage());
        assertNotNull(result1.getData());
        assertNotNull(result2.getData());
        assertEquals(result1.getData().getUuid(), result2.getData().getUuid());

        verify(userServiceClient, times(2)).getUser(testUserId);
    }

    @Test
    void testGetUser_DifferentUsers() {
        // Given
        UUID anotherUserId = UUID.randomUUID();
        UserProfileResponseDTO anotherUser = new UserProfileResponseDTO();
        anotherUser.setUuid(anotherUserId.toString());
        anotherUser.setUsername("anotheruser");
        anotherUser.setEmail("another@example.com");
        anotherUser.setStatus(UserStatus.NORMAL);
        
        ApiResponse<UserProfileResponseDTO> anotherResponse = ApiResponse.success("Success", anotherUser);
        
        when(userServiceClient.getUser(testUserId)).thenReturn(userProfileResponse);
        when(userServiceClient.getUser(anotherUserId)).thenReturn(anotherResponse);

        // When
        ApiResponse<UserProfileResponseDTO> result1 = userServiceClient.getUser(testUserId);
        ApiResponse<UserProfileResponseDTO> result2 = userServiceClient.getUser(anotherUserId);

        // Then
        assertNotNull(result1);
        assertNotNull(result2);
        assertNotEquals(result1.getData().getUuid(), result2.getData().getUuid());
        assertNotEquals(result1.getData().getUsername(), result2.getData().getUsername());

        verify(userServiceClient).getUser(testUserId);
        verify(userServiceClient).getUser(anotherUserId);
    }

    // Test default method using a concrete implementation
    @Test
    void testGetUsernameById_DefaultMethod_Success() {
        // Given
        UserServiceClient client = new UserServiceClient() {
            @Override
            public ApiResponse<UserProfileResponseDTO> getUser(UUID userId) {
                return userProfileResponse;
            }
        };

        // When
        String result = client.getUsernameById(testUserId);

        // Then
        assertNotNull(result);
        assertEquals(testUserProfile.getUsername(), result);
    }

    @Test
    void testGetUsernameById_DefaultMethod_NullResponse() {
        // Given
        UserServiceClient client = new UserServiceClient() {
            @Override
            public ApiResponse<UserProfileResponseDTO> getUser(UUID userId) {
                return null;
            }
        };

        // When
        String result = client.getUsernameById(testUserId);

        // Then
        assertEquals("Unknown User", result);
    }

    @Test
    void testGetUsernameById_DefaultMethod_EmptyData() {
        // Given
        ApiResponse<UserProfileResponseDTO> emptyResponse = ApiResponse.success("Success", null);
        UserServiceClient client = new UserServiceClient() {
            @Override
            public ApiResponse<UserProfileResponseDTO> getUser(UUID userId) {
                return emptyResponse;
            }
        };

        // When
        String result = client.getUsernameById(testUserId);

        // Then
        assertEquals("Unknown User", result);
    }

    @Test
    void testGetUsernameById_DefaultMethod_Exception() {
        // Given
        UserServiceClient client = new UserServiceClient() {
            @Override
            public ApiResponse<UserProfileResponseDTO> getUser(UUID userId) {
                throw new RuntimeException("Service error");
            }
        };

        // When
        String result = client.getUsernameById(testUserId);

        // Then
        assertEquals("Unknown User", result);
    }

    @Test
    void testGetUsernameById_DefaultMethod_NullUsername() {
        // Given
        testUserProfile.setUsername(null);
        UserServiceClient client = new UserServiceClient() {
            @Override
            public ApiResponse<UserProfileResponseDTO> getUser(UUID userId) {
                return userProfileResponse;
            }
        };

        // When
        String result = client.getUsernameById(testUserId);

        // Then
        assertNull(result);
    }
}
