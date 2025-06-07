package com.blps.lab1.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMessage {
    private Long reviewId;
    private String action; // CREATE, UPDATE, DELETE
    private String content;
    private Long userId;
    private Long productId;
} 