package com.blps.lab1.repositories;

import com.blps.lab1.entities.Response;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResponseRepository extends JpaRepository<Response, Long> {
    Optional<Response> findByReviewId(Long reviewId);
}
