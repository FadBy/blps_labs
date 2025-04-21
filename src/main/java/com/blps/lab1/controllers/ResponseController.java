package com.blps.lab1.controllers;

import com.blps.lab1.entities.Response;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.repositories.ResponseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/response")
public class ResponseController {
    private final ResponseRepository responseRepository;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_REVIEWS')")
    public Response getResponses(@RequestParam Long reviewId) {
        return responseRepository.findByReviewId(reviewId)
                .orElseThrow(() -> new ReviewInvalidException("Review id not found"));
    }

    @ExceptionHandler(ReviewInvalidException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleReportNotFound(ReviewInvalidException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
