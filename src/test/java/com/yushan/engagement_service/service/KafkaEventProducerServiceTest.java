package com.yushan.engagement_service.service;

import com.yushan.engagement_service.dto.event.CommentCreatedEvent;
import com.yushan.engagement_service.dto.event.ReviewCreatedEvent;
import com.yushan.engagement_service.dto.event.UserActivityEvent;
import com.yushan.engagement_service.dto.event.VoteCreatedEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaEventProducerServiceTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    @Mock
    private SendResult<String, Object> sendResult;

    @Mock
    private org.apache.kafka.clients.producer.RecordMetadata recordMetadata;

    @InjectMocks
    private KafkaEventProducerService kafkaEventProducerService;

    @BeforeEach
    void setUp() {
        // Inject test values
        ReflectionTestUtils.setField(kafkaEventProducerService, "commentEventsTopic", "test-comment-events");
        ReflectionTestUtils.setField(kafkaEventProducerService, "reviewEventsTopic", "test-review-events");
        ReflectionTestUtils.setField(kafkaEventProducerService, "voteEventsTopic", "test-vote-events");
        ReflectionTestUtils.setField(kafkaEventProducerService, "serviceName", "test-engagement-service");
    }

    @Test
    void publishCommentCreatedEvent_Success_ShouldPublishEvent() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();
        Integer chapterId = 100;
        String content = "Test comment";
        Boolean isSpoiler = false;

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString(), any(CommentCreatedEvent.class))).thenReturn(future);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(1L);

        // Act
        kafkaEventProducerService.publishCommentCreatedEvent(commentId, userId, chapterId, content, isSpoiler);

        // Assert
        verify(kafkaTemplate).send(eq("test-comment-events"), eq(commentId.toString()), any(CommentCreatedEvent.class));
    }

    @Test
    void publishCommentCreatedEvent_Exception_ShouldLogError() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();
        Integer chapterId = 100;
        String content = "Test comment";
        Boolean isSpoiler = false;

        when(kafkaTemplate.send(anyString(), anyString(), any(CommentCreatedEvent.class)))
                .thenThrow(new RuntimeException("Kafka error"));

        // Act
        kafkaEventProducerService.publishCommentCreatedEvent(commentId, userId, chapterId, content, isSpoiler);

        // Assert
        verify(kafkaTemplate).send(eq("test-comment-events"), eq(commentId.toString()), any(CommentCreatedEvent.class));
    }

    @Test
    void publishReviewCreatedEvent_Success_ShouldPublishEvent() {
        // Arrange
        Integer reviewId = 1;
        UUID reviewUuid = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Integer rating = 5;
        String title = "Test review";
        String content = "Test review content";
        Boolean isSpoiler = false;

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString(), any(ReviewCreatedEvent.class))).thenReturn(future);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(1L);

        // Act
        kafkaEventProducerService.publishReviewCreatedEvent(reviewId, reviewUuid, userId, rating, title, content, isSpoiler);

        // Assert
        verify(kafkaTemplate).send(eq("test-review-events"), eq(reviewId.toString()), any(ReviewCreatedEvent.class));
    }

    @Test
    void publishReviewCreatedEvent_Exception_ShouldLogError() {
        // Arrange
        Integer reviewId = 1;
        UUID reviewUuid = UUID.randomUUID();
        UUID userId = UUID.randomUUID();
        Integer rating = 5;
        String title = "Test review";
        String content = "Test review content";
        Boolean isSpoiler = false;

        when(kafkaTemplate.send(anyString(), anyString(), any(ReviewCreatedEvent.class)))
                .thenThrow(new RuntimeException("Kafka error"));

        // Act
        kafkaEventProducerService.publishReviewCreatedEvent(reviewId, reviewUuid, userId, rating, title, content, isSpoiler);

        // Assert
        verify(kafkaTemplate).send(eq("test-review-events"), eq(reviewId.toString()), any(ReviewCreatedEvent.class));
    }

    @Test
    void publishVoteCreatedEvent_Success_ShouldPublishEvent() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString(), any(VoteCreatedEvent.class))).thenReturn(future);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(1L);

        // Act
        kafkaEventProducerService.publishVoteCreatedEvent(voteId, userId);

        // Assert
        verify(kafkaTemplate).send(eq("test-vote-events"), eq(voteId.toString()), any(VoteCreatedEvent.class));
    }

    @Test
    void publishVoteCreatedEvent_Exception_ShouldLogError() {
        // Arrange
        Integer voteId = 1;
        UUID userId = UUID.randomUUID();

        when(kafkaTemplate.send(anyString(), anyString(), any(VoteCreatedEvent.class)))
                .thenThrow(new RuntimeException("Kafka error"));

        // Act
        kafkaEventProducerService.publishVoteCreatedEvent(voteId, userId);

        // Assert
        verify(kafkaTemplate).send(eq("test-vote-events"), eq(voteId.toString()), any(VoteCreatedEvent.class));
    }

    @Test
    void publishUserActivityEvent_Success_ShouldPublishEvent() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserActivityEvent event = new UserActivityEvent(userId, "test-service", "/test-endpoint", "GET", LocalDateTime.now());

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString(), any(UserActivityEvent.class))).thenReturn(future);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(1L);

        // Act
        kafkaEventProducerService.publishUserActivityEvent(event);

        // Assert
        verify(kafkaTemplate).send(eq("active"), eq(userId.toString()), eq(event));
    }

    @Test
    void publishUserActivityEvent_Exception_ShouldLogError() {
        // Arrange
        UUID userId = UUID.randomUUID();
        UserActivityEvent event = new UserActivityEvent(userId, "test-service", "/test-endpoint", "GET", LocalDateTime.now());

        when(kafkaTemplate.send(anyString(), anyString(), any(UserActivityEvent.class)))
                .thenThrow(new RuntimeException("Kafka error"));

        // Act
        kafkaEventProducerService.publishUserActivityEvent(event);

        // Assert
        verify(kafkaTemplate).send(eq("active"), eq(userId.toString()), eq(event));
    }

    @Test
    void publishEvent_Success_ShouldLogDebugMessage() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();
        Integer chapterId = 100;
        String content = "Test comment";
        Boolean isSpoiler = false;

        CompletableFuture<SendResult<String, Object>> future = CompletableFuture.completedFuture(sendResult);
        when(kafkaTemplate.send(anyString(), anyString(), any(CommentCreatedEvent.class))).thenReturn(future);
        when(sendResult.getRecordMetadata()).thenReturn(recordMetadata);
        when(recordMetadata.partition()).thenReturn(0);
        when(recordMetadata.offset()).thenReturn(1L);

        // Act
        kafkaEventProducerService.publishCommentCreatedEvent(commentId, userId, chapterId, content, isSpoiler);

        // Assert
        verify(kafkaTemplate).send(eq("test-comment-events"), eq(commentId.toString()), any(CommentCreatedEvent.class));
    }

    @Test
    void publishEvent_Failure_ShouldLogErrorMessage() {
        // Arrange
        Integer commentId = 1;
        UUID userId = UUID.randomUUID();
        Integer chapterId = 100;
        String content = "Test comment";
        Boolean isSpoiler = false;

        CompletableFuture<SendResult<String, Object>> future = new CompletableFuture<>();
        future.completeExceptionally(new RuntimeException("Kafka error"));
        when(kafkaTemplate.send(anyString(), anyString(), any(CommentCreatedEvent.class))).thenReturn(future);

        // Act
        kafkaEventProducerService.publishCommentCreatedEvent(commentId, userId, chapterId, content, isSpoiler);

        // Assert
        verify(kafkaTemplate).send(eq("test-comment-events"), eq(commentId.toString()), any(CommentCreatedEvent.class));
    }
}
