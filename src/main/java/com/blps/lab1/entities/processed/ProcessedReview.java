package com.blps.lab1.entities.processed;

import com.blps.lab1.entities.Employee;
import com.blps.lab1.entities.Vacancy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "processed_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessedReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private Double rating;
    private LocalDate date;
    private String problemCategory;
    private LocalDate processedDate;
    private String responseText;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    @ToString.Exclude
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;
} 