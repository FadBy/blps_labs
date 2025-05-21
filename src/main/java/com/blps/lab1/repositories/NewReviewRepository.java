package com.blps.lab1.repositories;

import com.blps.lab1.entities.unprocessed.NewReview;
import com.blps.lab1.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional(value = "transactionManager", rollbackFor = Exception.class)
public interface NewReviewRepository extends JpaRepository<NewReview, Long> {
    List<NewReview> findAll();
    List<NewReview> findAllByVacancy(Vacancy vacancy);
    List<NewReview> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
} 