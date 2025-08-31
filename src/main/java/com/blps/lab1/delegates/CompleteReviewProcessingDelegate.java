package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.Response;
import com.blps.lab1.repositories.ReviewRepository;
import com.blps.lab1.entities.ReviewStatus;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
@Slf4j
public class CompleteReviewProcessingDelegate implements JavaDelegate {
    private final ReviewRepository reviewRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Completing review processing...");
        
        // Получаем данные из переменных процесса
        String reviewJson = (String) execution.getVariable("review");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        Review review = mapper.readValue(reviewJson, Review.class);

        String responseJson = (String) execution.getVariable("response");

        Response response = mapper.readValue(responseJson, Response.class);
        
        if (review == null || response == null) {
            throw new RuntimeException("Review or Response not found in process variables");
        }
        
        // Обновляем статус отзыва на PROCESSED
        review.setStatus(ReviewStatus.PROCESSED);
        reviewRepository.save(review);
        
        log.info("Updated review ID: {} status to PROCESSED", review.getId());
        
        // Устанавливаем финальные переменные процесса
        execution.setVariable("processEndTime", LocalDateTime.now().toString());
        execution.setVariable("reviewStatus", ReviewStatus.PROCESSED.toString());
        execution.setVariable("processingCompleted", true);
        
        // Логируем завершение обработки
        log.info("Review processing completed successfully. Review ID: {}, Response ID: {}, Total processing time: {}",
                review.getId(), response.getId(), 
                LocalDateTime.now().toString());
        
        log.info("ReviewHandlingProcess completed for review ID: {}", review.getId());
    }
}
