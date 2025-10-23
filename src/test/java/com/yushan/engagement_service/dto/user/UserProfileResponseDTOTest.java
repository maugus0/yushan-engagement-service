package com.yushan.engagement_service.dto.user;

import com.yushan.engagement_service.enums.Gender;
import com.yushan.engagement_service.enums.UserStatus;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for UserProfileResponseDTO
 */
class UserProfileResponseDTOTest {

    @Test
    void testDefaultConstructor() {
        // Act
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Assert
        assertNotNull(dto);
        assertNull(dto.getUuid());
        assertNull(dto.getEmail());
        assertNull(dto.getUsername());
        assertNull(dto.getAvatarUrl());
        assertNull(dto.getProfileDetail());
        assertNull(dto.getBirthday());
        assertNull(dto.getGender());
        assertNull(dto.getIsAuthor());
        assertNull(dto.getIsAdmin());
        assertNull(dto.getStatus());
        assertNull(dto.getCreateTime());
        assertNull(dto.getUpdateTime());
        assertNull(dto.getLastActive());
        assertNull(dto.getLastLogin());
    }

    @Test
    void testSettersAndGetters() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        String uuid = "550e8400-e29b-41d4-a716-446655440001";
        String email = "test@example.com";
        String username = "testuser";
        String avatarUrl = "https://example.com/avatar.jpg";
        String profileDetail = "Test profile";
        Date birthday = new Date(946684800000L); // 2000-01-01
        Gender gender = Gender.MALE;
        Boolean isAuthor = true;
        Boolean isAdmin = false;
        UserStatus status = UserStatus.NORMAL;
        Date createTime = new Date(946684800000L);
        Date updateTime = new Date(946684800000L);
        Date lastActive = new Date(946684800000L);
        Date lastLogin = new Date(946684800000L);

        // Act
        dto.setUuid(uuid);
        dto.setEmail(email);
        dto.setUsername(username);
        dto.setAvatarUrl(avatarUrl);
        dto.setProfileDetail(profileDetail);
        dto.setBirthday(birthday);
        dto.setGender(gender);
        dto.setIsAuthor(isAuthor);
        dto.setIsAdmin(isAdmin);
        dto.setStatus(status);
        dto.setCreateTime(createTime);
        dto.setUpdateTime(updateTime);
        dto.setLastActive(lastActive);
        dto.setLastLogin(lastLogin);

        // Assert
        assertEquals(uuid, dto.getUuid());
        assertEquals(email, dto.getEmail());
        assertEquals(username, dto.getUsername());
        assertEquals(avatarUrl, dto.getAvatarUrl());
        assertEquals(profileDetail, dto.getProfileDetail());
        assertEquals(birthday, dto.getBirthday());
        assertEquals(gender, dto.getGender());
        assertEquals(isAuthor, dto.getIsAuthor());
        assertEquals(isAdmin, dto.getIsAdmin());
        assertEquals(status, dto.getStatus());
        assertEquals(createTime, dto.getCreateTime());
        assertEquals(updateTime, dto.getUpdateTime());
        assertEquals(lastActive, dto.getLastActive());
        assertEquals(lastLogin, dto.getLastLogin());
    }

    @Test
    void testBirthdayGetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setBirthday(null);

        // Act
        Date result = dto.getBirthday();

        // Assert
        assertNull(result);
    }

    @Test
    void testBirthdaySetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act
        dto.setBirthday(null);

        // Assert
        assertNull(dto.getBirthday());
    }

    @Test
    void testBirthdayGetterReturnsNewInstance() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setBirthday(originalDate);

        // Act
        Date result1 = dto.getBirthday();
        Date result2 = dto.getBirthday();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testCreateTimeGetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setCreateTime(null);

        // Act
        Date result = dto.getCreateTime();

        // Assert
        assertNull(result);
    }

    @Test
    void testCreateTimeSetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act
        dto.setCreateTime(null);

        // Assert
        assertNull(dto.getCreateTime());
    }

    @Test
    void testCreateTimeGetterReturnsNewInstance() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setCreateTime(originalDate);

        // Act
        Date result1 = dto.getCreateTime();
        Date result2 = dto.getCreateTime();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testUpdateTimeGetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setUpdateTime(null);

        // Act
        Date result = dto.getUpdateTime();

        // Assert
        assertNull(result);
    }

    @Test
    void testUpdateTimeSetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act
        dto.setUpdateTime(null);

        // Assert
        assertNull(dto.getUpdateTime());
    }

    @Test
    void testUpdateTimeGetterReturnsNewInstance() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setUpdateTime(originalDate);

        // Act
        Date result1 = dto.getUpdateTime();
        Date result2 = dto.getUpdateTime();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testLastActiveGetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setLastActive(null);

        // Act
        Date result = dto.getLastActive();

        // Assert
        assertNull(result);
    }

    @Test
    void testLastActiveSetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act
        dto.setLastActive(null);

        // Assert
        assertNull(dto.getLastActive());
    }

    @Test
    void testLastActiveGetterReturnsNewInstance() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setLastActive(originalDate);

        // Act
        Date result1 = dto.getLastActive();
        Date result2 = dto.getLastActive();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testLastLoginGetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        dto.setLastLogin(null);

        // Act
        Date result = dto.getLastLogin();

        // Assert
        assertNull(result);
    }

    @Test
    void testLastLoginSetterWithNullValue() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act
        dto.setLastLogin(null);

        // Assert
        assertNull(dto.getLastLogin());
    }

    @Test
    void testLastLoginGetterReturnsNewInstance() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();
        Date originalDate = new Date(946684800000L);
        dto.setLastLogin(originalDate);

        // Act
        Date result1 = dto.getLastLogin();
        Date result2 = dto.getLastLogin();

        // Assert
        assertNotSame(originalDate, result1);
        assertNotSame(result1, result2);
        assertEquals(originalDate.getTime(), result1.getTime());
        assertEquals(originalDate.getTime(), result2.getTime());
    }

    @Test
    void testAllGenderValues() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act & Assert
        dto.setGender(Gender.MALE);
        assertEquals(Gender.MALE, dto.getGender());

        dto.setGender(Gender.FEMALE);
        assertEquals(Gender.FEMALE, dto.getGender());

        dto.setGender(Gender.UNKNOWN);
        assertEquals(Gender.UNKNOWN, dto.getGender());
    }

    @Test
    void testAllUserStatusValues() {
        // Arrange
        UserProfileResponseDTO dto = new UserProfileResponseDTO();

        // Act & Assert
        dto.setStatus(UserStatus.NORMAL);
        assertEquals(UserStatus.NORMAL, dto.getStatus());

        dto.setStatus(UserStatus.SUSPENDED);
        assertEquals(UserStatus.SUSPENDED, dto.getStatus());

        dto.setStatus(UserStatus.BANNED);
        assertEquals(UserStatus.BANNED, dto.getStatus());
    }
}
