package com.blps.lab1.services;

import com.blps.lab1.entities.Review;
import org.springframework.stereotype.Service;

@Service
public class HRService {
    public String escalateReview(Review review) {
        return String.format("Благодарим за отзыв! Мы обязательно улучшим %s", review.getProblemCategory());
    }
}
