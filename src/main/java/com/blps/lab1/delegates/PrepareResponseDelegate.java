package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.Response;
import com.blps.lab1.entities.ResponseStatus;
import com.blps.lab1.repositories.ResponseRepository;
import com.blps.lab1.services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.variable.Variables;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

import static org.camunda.spin.Spin.JSON;

@Component
@RequiredArgsConstructor
@Slf4j
public class PrepareResponseDelegate implements JavaDelegate {
    private final ReviewService reviewService;
    @Override

    public void execute(DelegateExecution execution) throws Exception {
        log.info("Preparing response for review...");

        String reviewJson = (String) execution.getVariable("review");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Review review = mapper.readValue(reviewJson, Review.class);

        if (review == null) {
            throw new RuntimeException("Review not found in process variables");
        }
        
        String message = (String)execution.getVariable("message");

        var response = reviewService.prepareResponse(review, message);

        execution.setVariable("response", mapper.writeValueAsString(response));
        
        log.info("Response preparation completed successfully");
    }
}
