package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.Response;
import com.blps.lab1.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class GenerateAnalyticsDelegate implements JavaDelegate {
    private final ReportService reportService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Generating analytics report...");
        
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
        
        log.info("Generating analytics for review ID: {} and response ID: {}", 
                review.getId(), response.getId());
        
        // Генерируем отчет за текущий период
        LocalDate currentDate = LocalDate.now();
        var report = reportService.findReportByPeriod(currentDate);
        
        log.info("Analytics report generated for period ending: {}", report.getPeriod());
        
        // Создаем аналитическую сводку
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("reportId", report.getId());
        analytics.put("reportPeriod", report.getPeriod());
        analytics.put("averageRating", report.getAverageRating());
        analytics.put("negativeReviewsCount", report.getNegativeReviewsCount());
        analytics.put("currentReviewRating", review.getRating());
        analytics.put("currentReviewStatus", review.getStatus());
        analytics.put("responseId", response.getId());
        analytics.put("responseDate", response.getPublishDate());
        
        // Сохраняем аналитику в переменных процесса
        execution.setVariable("analyticsReport", analytics);
        execution.setVariable("reportId", report.getId());
        execution.setVariable("averageRating", report.getAverageRating());
        execution.setVariable("negativeReviewsCount", report.getNegativeReviewsCount());
        execution.setVariable("analyticsGenerated", true);
        
        log.info("Analytics report generated successfully. Report ID: {}, Avg Rating: {}, Negative Count: {}", 
                report.getId(), report.getAverageRating(), report.getNegativeReviewsCount());
    }
}
