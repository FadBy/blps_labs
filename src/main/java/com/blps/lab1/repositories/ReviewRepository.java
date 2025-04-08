package com.blps.lab1.repositories;

import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.ReviewStatus;
import com.blps.lab1.entities.Vacancy;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends Repository<Review, Long> {
    Review save(Review review);
    List<Review> findAllByStatus(ReviewStatus reviewStatus);
    List<Review> findAllByVacancy(Vacancy vacancy);
    @Transactional
    @Modifying
    @Query("UPDATE Review r SET r.status = :status WHERE r.id = :id")
    void updateStatus(@Param("id") Long id, @Param("status") ReviewStatus status);
    List<Review> findAllByDateBetween(LocalDate startDate, LocalDate endDate);
}
