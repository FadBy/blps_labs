package com.blps.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewProcessingMessage {
    private Long reviewId;
    private String action; // PROCESS, ESCALATE
    private Double rating;
    private String text;
    private String employeeName;
    private String vacancyTitle;
} 