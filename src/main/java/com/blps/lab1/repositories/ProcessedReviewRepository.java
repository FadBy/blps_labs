package com.blps.lab1.repositories;

import com.blps.lab1.entities.processed.ProcessedReview;
import com.blps.lab1.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(value = "transactionManager", rollbackFor = Exception.class)
public interface ProcessedReviewRepository extends JpaRepository<ProcessedReview, Long> {
    List<ProcessedReview> findAll();
    List<ProcessedReview> findAllByVacancy(Vacancy vacancy);
    List<ProcessedReview> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
} 