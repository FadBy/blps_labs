package com.blps.lab1.services;

import com.blps.lab1.entities.Response;
import com.blps.lab1.entities.ResponseStatus;
import com.blps.lab1.entities.Review;
import org.springframework.stereotype.Service;

@Service
public class HRService {
    public String escalateReview(Review review) {
        // TODO Посылаем ревью HR
        return String.format("Благодарим за отзыв! Мы обязательно улучшим %s", review.getProblemCategory());
    }
}
