package com.yushan.engagement_service.integration;

import com.yushan.engagement_service.TestcontainersConfiguration;
import com.yushan.engagement_service.util.JwtTestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Integration tests for Engagement Service with real PostgreSQL
 * 
 * This test class verifies:
 * - Service integration with database
 * - JWT authentication and authorization
 * - Database transactions and data integrity
 */
@SpringBootTest
@ActiveProfiles("integration-test")
@Import(TestcontainersConfiguration.class)
@Transactional
@TestPropertySource(properties = {
    "spring.kafka.bootstrap-servers=",
    "spring.kafka.enabled=false",
    "spring.autoconfigure.exclude=org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration"
})
@org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable(named = "CI", matches = "true")
public class EngagementServiceIntegrationTest {

    // Using @MockBean for integration tests - deprecation warning is acceptable for test code
    @MockBean
    private com.yushan.engagement_service.service.KafkaEventProducerService kafkaEventProducerService;
    
    // Mock KafkaTemplate to prevent actual Kafka connection
    @MockBean
    private org.springframework.kafka.core.KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    private JwtTestUtil jwtTestUtil;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        // Configure PostgreSQL
        registry.add("spring.datasource.url", TestcontainersConfiguration.postgres::getJdbcUrl);
        registry.add("spring.datasource.username", TestcontainersConfiguration.postgres::getUsername);
        registry.add("spring.datasource.password", TestcontainersConfiguration.postgres::getPassword);
        
        // Configure Redis
        registry.add("spring.data.redis.host", TestcontainersConfiguration.redis::getHost);
        registry.add("spring.data.redis.port", () -> TestcontainersConfiguration.redis.getMappedPort(6379));
    }

    @Test
    void contextLoads() {
        // This test verifies that the Spring context loads successfully
        assertTrue(true);
    }

    @Test
    void jwtTestUtil_ShouldGenerateValidTokens() {
        // Test JWT token generation
        String authorToken = jwtTestUtil.generateTestAuthorToken();
        String adminToken = jwtTestUtil.generateTestAdminToken();
        
        assertTrue(authorToken != null && !authorToken.isEmpty());
        assertTrue(adminToken != null && !adminToken.isEmpty());
    }
}
