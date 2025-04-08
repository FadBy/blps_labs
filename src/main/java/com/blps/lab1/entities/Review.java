package com.blps.lab1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private Double rating;
    private LocalDate date;
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
    private String problemCategory;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    @ToString.Exclude  // Чтобы избежать рекурсии в toString()
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;
}