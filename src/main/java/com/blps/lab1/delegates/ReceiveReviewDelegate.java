package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.repositories.ReviewRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import static org.camunda.spin.Spin.JSON;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReceiveReviewDelegate implements JavaDelegate {
    private final ReviewRepository reviewRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Receiving review for processing...");
        
        // Получаем ID отзыва из переменных процесса
        Long reviewId = (Long) execution.getVariable("reviewId");
        if (reviewId == null) {
            throw new RuntimeException("Review ID not found in process variables");
        }
        
        // Получаем отзыв из базы данных
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found with ID: " + reviewId));
        
        log.info("Received review ID: {} from employee: {} for vacancy: {}", 
                reviewId, review.getEmployee().getName(), review.getVacancy().getTitle());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        execution.setVariable("review", mapper.writeValueAsString(review));
        execution.setVariable("reviewId", reviewId);
        execution.setVariable("processStartTime", java.time.LocalDateTime.now().toString());
        execution.setVariable("reviewStatus", review.getStatus().toString());
        
        // Логируем начало обработки
        log.info("Review processing started. Review ID: {}, Rating: {}, Status: {}", 
                reviewId, review.getRating(), review.getStatus());
    }
}
