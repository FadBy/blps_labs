package com.blps.lab1.service;

import com.blps.lab1.dto.ReviewProcessingMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Service
public class ReviewConsumer {
    // Хранилище статусов обработки отзывов
    private final Map<Long, String> reviewProcessingStatus = new ConcurrentHashMap<>();

    @KafkaListener(topics = "review-processing", groupId = "review-processing-group")
    public void processReview(ReviewProcessingMessage message) {
        log.info("Received review for processing: {}", message);
        
        try {
            // Имитация обработки
            Thread.sleep(1000);
            
            String status = message.getAction().equals("ESCALATE") 
                ? "ESCALATED_TO_MANAGER" 
                : "PROCESSED_SUCCESSFULLY";
            
            reviewProcessingStatus.put(message.getReviewId(), status);
            log.info("Review {} processed with status: {}", message.getReviewId(), status);
        } catch (InterruptedException e) {
            log.error("Error processing review: {}", e.getMessage());
            reviewProcessingStatus.put(message.getReviewId(), "PROCESSING_ERROR");
        }
    }

    public String getReviewStatus(Long reviewId) {
        return reviewProcessingStatus.getOrDefault(reviewId, "NOT_FOUND");
    }
} 