package com.blps.lab1.controllers;

import com.blps.lab1.dto.ReviewDTO;
import com.blps.lab1.entities.Review;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.services.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_REVIEWS')")
    public Review createReview(@RequestBody ReviewDTO review) {
        return reviewService.save(review);
    }

    @GetMapping("/process_new")
    @PreAuthorize("hasAuthority('ANSWER_REVIEWS')")
    public ResponseEntity<Resource> processNewReviews() throws Exception {
        Resource pdfResource = reviewService.processNewReviews();

        if (pdfResource == null) {
            return ResponseEntity
                    .status(HttpStatus.NO_CONTENT)
                    .body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=reviews_report.pdf");
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(pdfResource.contentLength())
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdfResource);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorMessage> handleException(ReviewInvalidException exception) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(new ErrorMessage(exception.getMessage()));
    }

    @GetMapping
    @PreAuthorize("hasAuthority('READ_REVIEWS')")
    public List<Review> getReviewsByVacancy(@RequestParam Long vacancyId) {
        return reviewService.getReviewsByVacancy(vacancyId);
    }
}