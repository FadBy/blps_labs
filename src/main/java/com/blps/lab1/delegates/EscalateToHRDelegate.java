package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.services.HRService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EscalateToHRDelegate implements JavaDelegate {
    private final HRService hrService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Escalating review to HR...");

        // Получаем отзыв из переменных процесса
        // Review review = (Review) execution.getVariable("review");
        // if (review == null) {
        //    throw new RuntimeException("Review not found in process variables");
        //}

        String hrResponse = hrService.escalateReview((String) execution.getVariable("problemCategory"));

        execution.setVariable("message", hrResponse);

        log.info("Escalated review to HR successfully");
    }
}
