package com.blps.lab1.service;

import com.blps.lab1.dto.ReviewProcessingMessage;
import com.blps.lab1.entities.Review;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaReviewService {
    private final KafkaTemplate<String, ReviewProcessingMessage> kafkaTemplate;
    private static final String REVIEW_TOPIC = "review-processing";

    public void sendReviewForProcessing(Long reviewId, String action, Double rating, String text, String employeeName, String vacancyTitle) {
        ReviewProcessingMessage message = new ReviewProcessingMessage(
                reviewId,
                action,
                rating,
                text,
                employeeName,
                vacancyTitle
        );
        
        log.info("Sending message to Kafka: {}", message);
        kafkaTemplate.send("review-processing", message);
    }

    @KafkaListener(topics = REVIEW_TOPIC, groupId = "review-processing-group")
    public void processReviewMessage(ReviewProcessingMessage message) {
        log.info("Received review processing message: {}", message);
        // Здесь будет логика обработки сообщения
        // Пока просто логируем
        if ("ESCALATE".equals(message.getAction())) {
            log.info("Review {} needs escalation", message.getReviewId());
        } else {
            log.info("Processing review {} normally", message.getReviewId());
        }
    }
} 