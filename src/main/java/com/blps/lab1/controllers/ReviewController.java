package com.blps.lab1.controllers;

import com.blps.lab1.dto.ReviewDTO;
import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.Vacancy;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    @Autowired
    private ReviewService reviewService;

    @PostMapping
    public Review createReview(@RequestBody ReviewDTO review) {
        return reviewService.save(review);
    }

    @GetMapping("/process_new")
    public List<Review> processNewReviews() {
        return reviewService.processNewReviews();
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(ReviewInvalidException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @GetMapping
    public List<Review> getReviewsByVacancy(@RequestParam Long vacancyId) {
        return reviewService.getReviewsByVacancy(vacancyId);
    }
}
