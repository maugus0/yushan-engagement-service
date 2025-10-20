package com.yushan.engagement_service.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

/**
 * Kafka event producer service for publishing engagement events
 * 
 * This service publishes events to Kafka topics for consumption by:
 * - Analytics Service: Track user behavior and engagement metrics
 * - Gamification Service: Award points and unlock achievements
 * - Content Service: Update engagement counts
 * - User Service: Update user activity feeds
 */
@Slf4j
@Service
public class KafkaEventProducerService {

    @Autowired
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Value("${spring.kafka.producer.topic.comment-events:comment-events}")
    private String commentEventsTopic;

    @Value("${spring.kafka.producer.topic.review-events:review-events}")
    private String reviewEventsTopic;

    @Value("${spring.kafka.producer.topic.vote-events:vote-events}")
    private String voteEventsTopic;

    @Value("${spring.application.name:engagement-service}")
    private String serviceName;

    private static final String EVENT_VERSION = "1.0";

    /**
     * Generic method to publish events to Kafka
     */
    private void publishEvent(String topic, String key, Object event) {
        CompletableFuture<SendResult<String, Object>> future = kafkaTemplate.send(topic, key, event);
        
        future.whenComplete((result, ex) -> {
            if (ex == null) {
                log.debug("Successfully sent event to topic: {}, partition: {}, offset: {}", 
                        topic, result.getRecordMetadata().partition(), result.getRecordMetadata().offset());
            } else {
                log.error("Failed to send event to topic: {}", topic, ex);
            }
        });
    }

    /**
     * Publish comment event (placeholder for future implementation)
     */
    public void publishCommentEvent(String eventType, String commentId, Object eventData) {
        try {
            publishEvent(commentEventsTopic, commentId, eventData);
            log.info("Published {} event for comment ID: {}", eventType, commentId);
        } catch (Exception e) {
            log.error("Failed to publish {} event for comment ID: {}", eventType, commentId, e);
        }
    }

    /**
     * Publish review event (placeholder for future implementation)
     */
    public void publishReviewEvent(String eventType, String reviewId, Object eventData) {
        try {
            publishEvent(reviewEventsTopic, reviewId, eventData);
            log.info("Published {} event for review ID: {}", eventType, reviewId);
        } catch (Exception e) {
            log.error("Failed to publish {} event for review ID: {}", eventType, reviewId, e);
        }
    }

    /**
     * Publish vote event (placeholder for future implementation)
     */
    public void publishVoteEvent(String eventType, String voteId, Object eventData) {
        try {
            publishEvent(voteEventsTopic, voteId, eventData);
            log.info("Published {} event for vote ID: {}", eventType, voteId);
        } catch (Exception e) {
            log.error("Failed to publish {} event for vote ID: {}", eventType, voteId, e);
        }
    }
}
