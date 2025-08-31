package com.blps.lab1.services;

import com.blps.lab1.entities.Review;
import org.springframework.stereotype.Service;

@Service
public class TemplateService {
    public String createResponseTemplate(double rating) {
        if (rating >= 4.5) {
            return "Wow, Thank you for review";
        } else {
            return "Thank you for review";
        }
    }
}
