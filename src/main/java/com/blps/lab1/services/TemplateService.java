package com.blps.lab1.services;

import com.blps.lab1.entities.Review;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {
    public String createResponseTemplate(Review review) {
        if (review.getRating() >= 4.5) {
            return "Благодарим за отзыв! Мы рады что вам понравилось у нас работать";
        } else {
            return "Благодарим за отзыв! Мы учтем и исправим недочеты, спасибо что работали у нас";
        }
    }
}
