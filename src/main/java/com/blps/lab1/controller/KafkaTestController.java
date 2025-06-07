package com.blps.lab1.controller;

import com.blps.lab1.dto.ReviewProcessingMessage;
import com.blps.lab1.service.KafkaReviewService;
import com.blps.lab1.service.ReviewConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/kafka/test")
@RequiredArgsConstructor
public class KafkaTestController {
    private final KafkaReviewService kafkaReviewService;
    private final ReviewConsumer reviewConsumer;

    @PostMapping("/send")
    public String sendTestMessage(@RequestBody ReviewProcessingMessage message) {
        kafkaReviewService.sendReviewForProcessing(
            message.getReviewId(),
            message.getAction(),
            message.getRating(),
            message.getText(),
            message.getEmployeeName(),
            message.getVacancyTitle()
        );
        return "Message sent successfully";
    }

    @GetMapping("/status/{reviewId}")
    public String getReviewStatus(@PathVariable Long reviewId) {
        return "Review status: " + reviewConsumer.getReviewStatus(reviewId);
    }
} 