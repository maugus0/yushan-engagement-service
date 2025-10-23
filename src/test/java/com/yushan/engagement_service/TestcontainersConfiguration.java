package com.yushan.engagement_service;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.test.context.DynamicPropertyRegistry;
import javax.sql.DataSource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
@SuppressWarnings("resource")
public class TestcontainersConfiguration {

    public static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"));
    public static GenericContainer<?> redis = new GenericContainer<>(DockerImageName.parse("redis:latest")).withExposedPorts(6379);

    static {
        try {
            postgres.start();
            redis.start();
            
            // Register shutdown hooks to properly close containers
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                try {
                    postgres.close();
                    redis.close();
                } catch (Exception e) {
                    // Ignore exceptions during shutdown
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException("Failed to start testcontainers", e);
        }
    }

    @Bean
    PostgreSQLContainer<?> postgresContainer() {
        return postgres;
    }

    @Bean
    DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName(postgres.getDriverClassName())
                .url(postgres.getJdbcUrl())
                .username(postgres.getUsername())
                .password(postgres.getPassword())
                .build();
    }

    @Bean
    GenericContainer<?> redisContainer() {
        return redis;
    }

    public static void configureProperties(DynamicPropertyRegistry registry) {
        // Configure PostgreSQL
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
        
        // Configure Redis
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(6379));
    }
}
