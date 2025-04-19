package com.blps.lab1.services;

import com.blps.lab1.entities.Report;
import com.blps.lab1.entities.Review;
import com.blps.lab1.repositories.ReportRepository;
import com.blps.lab1.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ReportService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ReportRepository reportRepository;

    @Scheduled(cron = "0 0 9 * * MON")
    @Transactional
    public void generateReportLastWeek() {
        LocalDate now = LocalDate.now();
        LocalDate endOfLastWeek = now.minusDays(now.getDayOfWeek().getValue());
        LocalDate startOfLastWeek = endOfLastWeek.minusDays(6);

        generateReportForPeriod(startOfLastWeek, endOfLastWeek);
    }

    @Transactional
    public Report findReportByPeriod(LocalDate date) {
        LocalDate weekStart = date.with(DayOfWeek.MONDAY);
        LocalDate weekEnd = date.with(DayOfWeek.SUNDAY);

        Optional<Report> existingReport = reportRepository.findByPeriodBetween(weekStart, weekEnd)
                .stream()
                .findFirst();

        return existingReport.orElseGet(() -> generateReportForPeriod(weekStart, weekEnd));
    }

    private Report generateReportForPeriod(LocalDate startDate, LocalDate endDate) {
        List<Review> weeklyReviews = getReviewsForPeriod(startDate, endDate);
        ReportMetrics metrics = calculateMetrics(weeklyReviews);
        return saveOrUpdateReport(endDate, metrics, startDate);
    }

    private List<Review> getReviewsForPeriod(LocalDate startDate, LocalDate endDate) {
        return reviewRepository.findAllByDateBetween(startDate, endDate);
    }

    private ReportMetrics calculateMetrics(List<Review> reviews) {
        double avgRating = reviews.stream()
                .mapToDouble(Review::getRating)
                .average()
                .orElse(0.0);

        long negativeCount = reviews.stream()
                .filter(r -> r.getRating() < 3.5)
                .count();

        return new ReportMetrics(avgRating, negativeCount);
    }

    private Report saveOrUpdateReport(LocalDate periodEnd, ReportMetrics metrics, LocalDate periodStart) {
        Report report = reportRepository.findByPeriod(periodEnd)
                .orElse(new Report());

        report.setPeriod(periodEnd);
        report.setAverageRating(metrics.avgRating());
        report.setNegativeReviewsCount((int) metrics.negativeCount());

        logReportAction(report, periodStart, periodEnd, metrics);
        return reportRepository.save(report);
    }

    private void logReportAction(Report report, LocalDate startDate, LocalDate endDate, ReportMetrics metrics) {
        log.info("{} отчет за период с {} по {}: средний рейтинг {}, негативных отзывов {}",
                report.getId() == null ? "Создан новый" : "Обновлен",
                startDate, endDate, metrics.avgRating(), metrics.negativeCount());
    }

    private record ReportMetrics(double avgRating, long negativeCount) {}
}
