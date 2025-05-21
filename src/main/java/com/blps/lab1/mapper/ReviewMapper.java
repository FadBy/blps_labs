package com.blps.lab1.mapper;

import com.blps.lab1.dto.ReviewDTO;
import com.blps.lab1.entities.unprocessed.NewReview;
import com.blps.lab1.entities.processed.ProcessedReview;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class ReviewMapper {
    
    public NewReview toNewReview(ReviewDTO dto) {
        return NewReview.builder()
                .text(dto.getText())
                .rating(dto.getRating())
                .date(dto.getDate())
                .problemCategory(dto.getProblemCategory())
                .build();
    }

    public ProcessedReview toProcessedReview(NewReview newReview) {
        return ProcessedReview.builder()
                .text(newReview.getText())
                .rating(newReview.getRating())
                .date(newReview.getDate())
                .problemCategory(newReview.getProblemCategory())
                .processedDate(LocalDate.now())
                .vacancy(newReview.getVacancy())
                .employee(newReview.getEmployee())
                .build();
    }
} 