package com.blps.lab1.services;

import com.blps.lab1.dto.ReviewDTO;
import com.blps.lab1.entities.*;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.repositories.EmployeeRepository;
import com.blps.lab1.repositories.ResponseRepository;
import com.blps.lab1.repositories.ReviewRepository;
import com.blps.lab1.repositories.VacancyRepository;
import com.blps.lab1.utils.TransactionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReviewService {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private ResponseRepository responseRepository;
    @Autowired
    private HRService hrService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private VacancyRepository vacancyRepository;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private TransactionHelper transactionHelper;

    @Scheduled(fixedRate = 60000)
    public List<Review> processNewReviews() {
        System.out.println("processNewReviews");
        List<Review> reviews = reviewRepository.findAllByStatus(ReviewStatus.NEW);
        for (var review : reviews) {
           processReview(review);
        }
        return reviews;
    }

    public Review save(ReviewDTO review) {
        if (review.getRating() < 1.0 || review.getRating() > 5.0) {
            throw new ReviewInvalidException("Rating must be between 1 and 5");
        }
        var employee = employeeRepository.findByName(review.getEmployeeName())
                .orElseThrow(() -> new ReviewInvalidException("Employee not found"));
        var vacancy = vacancyRepository.findVacancyById(review.getVacancyId())
                .orElseThrow(() -> new ReviewInvalidException("Vacancy not found"));
        var newReview = new Review();
        newReview.setRating(review.getRating());
        newReview.setText(review.getText());
        newReview.setStatus(ReviewStatus.NEW);
        newReview.setDate(review.getDate());
        newReview.setEmployee(employee);
        newReview.setStatus(ReviewStatus.NEW);
        newReview.setProblemCategory(review.getProblemCategory());
        newReview.setVacancy(vacancy);
        return reviewRepository.save(newReview);
    }

    public List<Review> getReviewsByVacancy(Long vacancyId) {
        Vacancy vacancy = vacancyRepository.findById(vacancyId).orElse(null);
        if (vacancy == null) {
            return new ArrayList<>();
        }
        return reviewRepository.findAllByVacancy(vacancy);
    }

    private void processReview(Review review) {
        String message;
        if (review.getRating() < 3.5) {
            message = hrService.escalateReview(review);
        } else {
            message = templateService.createResponseTemplate(review);
        }
        var response = prepareResponse(review, message);
        publishResponse(response);
    }

    private void publishResponse(Response response) {
        transactionHelper.executeInTransaction(() -> {
            responseRepository.save(response);
            reviewRepository.updateStatus(response.getReview().getId(), ReviewStatus.PROCESSED);
            sendReportToHR(response);
        });
    }

    private Response prepareResponse(Review review, String message) {
        Response response = new Response();
        response.setText(message);
        response.setStatus(ResponseStatus.PUBLISHED);
        response.setPublishDate(LocalDate.now());
        response.setReview(review);
        return response;
    }

    private void sendReportToHR(Response response) {
        // Отправляем отчет HR
    }
}
