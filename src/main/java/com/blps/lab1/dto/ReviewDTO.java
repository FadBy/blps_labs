package com.blps.lab1.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class ReviewDTO {
    private String text;
    private Double rating;
    private LocalDate date;
    private Long vacancyId;
    private String employeeName;
    private String problemCategory;
}
