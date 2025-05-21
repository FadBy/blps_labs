package com.blps.lab1.entities.unprocessed;

import com.blps.lab1.entities.Employee;
import com.blps.lab1.entities.Vacancy;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "new_reviews")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private Double rating;
    private LocalDate date;
    private String problemCategory;

    @ManyToOne
    @JoinColumn(name = "vacancy_id")
    @ToString.Exclude
    private Vacancy vacancy;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    @ToString.Exclude
    private Employee employee;
} 