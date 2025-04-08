package com.blps.lab1.controllers;

import com.blps.lab1.entities.Response;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.ReportNotFoundException;
import com.blps.lab1.exceptions.ReviewInvalidException;
import com.blps.lab1.repositories.ResponseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/response")
public class ResponseController {
    @Autowired
    private ResponseRepository responseRepository;

    @GetMapping
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
