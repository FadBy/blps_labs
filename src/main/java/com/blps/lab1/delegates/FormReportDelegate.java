package com.blps.lab1.delegates;

import com.blps.lab1.entities.Report;
import com.blps.lab1.entities.Review;
import com.blps.lab1.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class FormReportDelegate implements JavaDelegate {
    private final ReportService reportService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        LocalDate now = LocalDate.now();
        LocalDate weekStart = now.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = now.with(DayOfWeek.SUNDAY).plusDays(1);

        var report = new Report();

        System.out.println(weekStart);
        System.out.println(weekEnd);

        List<Review> weeklyReviews = reportService.getReviewsForPeriod(weekStart, weekEnd);
        ReportService.ReportMetrics metrics = reportService.calculateMetrics(weeklyReviews);

        report.setPeriod(weekEnd);
        report.setAverageRating(metrics.avgRating());
        report.setNegativeReviewsCount((int) metrics.negativeCount());

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        execution.setVariable("report", mapper.writeValueAsString(report));
    }
}
