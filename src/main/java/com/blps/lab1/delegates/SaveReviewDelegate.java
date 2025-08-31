package com.blps.lab1.delegates;

import com.blps.lab1.dto.ReviewDTO;
import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.ReviewStatus;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.repositories.ReviewRepository;
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
public class SaveReviewDelegate implements JavaDelegate {
    private final ReviewRepository reviewRepository;
    private final ReviewService reviewService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Saving review...");

        // Получаем переменные с проверкой на null
        String text = (String) execution.getVariable("text");
        String ratingStr = (String) execution.getVariable("rating");
        Long vacancyId = (Long) execution.getVariable("vacancyID");
        String employeeName = (String) execution.getVariable("EmployeeName");
        String problemCategory = (String) execution.getVariable("problemCategory");

        log.info("Text: {}", text);
        log.info("Rating: {}", ratingStr);
        log.info("VacancyID: {}", vacancyId);
        log.info("EmployeeName: {}", employeeName);
        log.info("ProblemCategory: {}", problemCategory);

        // Проверяем, что все необходимые переменные получены
        if (text == null || ratingStr == null || vacancyId == null || employeeName == null || problemCategory == null) {
            log.error("Some required variables are null: text={}, rating={}, vacancyId={}, employeeName={}, problemCategory={}", 
                     text, ratingStr, vacancyId, employeeName, problemCategory);
            throw new IllegalArgumentException("Required variables cannot be null");
        }

        Double rating;
        try {
            rating = Double.parseDouble(ratingStr);
        } catch (NumberFormatException e) {
            log.error("Cannot parse rating '{}' as number", ratingStr);
            throw new IllegalArgumentException("Rating must be a valid number");
        }

        ReviewDTO reviewDTO = ReviewDTO.builder()
                .text(text)
                .rating(rating)
                .date(LocalDate.now())
                .vacancyId(vacancyId)
                .employeeName(employeeName)
                .problemCategory(problemCategory)
                .build();

        var review = reviewService.save(reviewDTO);

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        execution.setVariable("review", mapper.writeValueAsString(review));

        log.info("Review saved successfully with ID: {}", review.getId());
    }
}
