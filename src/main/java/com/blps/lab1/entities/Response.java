package com.blps.lab1.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Response {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    @Enumerated(EnumType.STRING)
    private ResponseStatus status;
    private LocalDate publishDate;

    @OneToOne
    @JoinColumn(name = "review_id")
    @ToString.Exclude
    private Review review;
}