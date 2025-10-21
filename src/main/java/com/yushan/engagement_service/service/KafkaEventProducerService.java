package com.yushan.engagement_service.service;

import com.yushan.engagement_service.dto.event.CommentCreatedEvent;
import com.yushan.engagement_service.dto.event.ReviewCreatedEvent;
import com.yushan.engagement_service.dto.event.UserActivityEvent;
import com.yushan.engagement_service.dto.event.VoteCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;

import java.util.UUID;
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
     * Publish comment created event
     */
    public void publishCommentCreatedEvent(Integer commentId, UUID userId, Integer chapterId, 
                                         String content, Boolean isSpoiler) {
        try {
            CommentCreatedEvent event = CommentCreatedEvent.builder()
                    .commentId(commentId)
                    .userId(userId)
                    .build();
            
            publishEvent(commentEventsTopic, commentId.toString(), event);
            log.info("Published COMMENT_CREATED event for comment ID: {}, user: {}", commentId, userId);
        } catch (Exception e) {
            log.error("Failed to publish COMMENT_CREATED event for comment ID: {}", commentId, e);
        }
    }

    /**
     * Publish review created event
     */
    public void publishReviewCreatedEvent(Integer reviewId, UUID reviewUuid, UUID userId, 
                                        Integer rating, String title, 
                                        String content, Boolean isSpoiler) {
        try {
            ReviewCreatedEvent event = ReviewCreatedEvent.builder()
                    .reviewId(reviewId)
                    .userId(userId)
                    .rating(rating)
                    .build();
            
            publishEvent(reviewEventsTopic, reviewId.toString(), event);
            log.info("Published REVIEW_CREATED event for review ID: {}, user: {}", reviewId, userId);
        } catch (Exception e) {
            log.error("Failed to publish REVIEW_CREATED event for review ID: {}", reviewId, e);
        }
    }

    /**
     * Publish vote created event
     */
    public void publishVoteCreatedEvent(Integer voteId, UUID userId) {
        try {
            VoteCreatedEvent event = VoteCreatedEvent.builder()
                    .voteId(voteId)
                    .userId(userId)
                    .build();
            
            publishEvent(voteEventsTopic, voteId.toString(), event);
            log.info("Published VOTE_CREATED event for vote ID: {}, user: {}", voteId, userId);
        } catch (Exception e) {
            log.error("Failed to publish VOTE_CREATED event for vote ID: {}", voteId, e);
        }
    }

    /**
     * Publish user activity event
     */
    public void publishUserActivityEvent(UserActivityEvent event) {
        try {
            publishEvent("active", event.userId().toString(), event);
            log.info("Published user activity event for user: {}, service: {}, endpoint: {}", 
                     event.userId(), event.serviceName(), event.endpoint());
        } catch (Exception e) {
            log.error("Failed to publish user activity event for user: {}", event.userId(), e);
        }
    }
}
