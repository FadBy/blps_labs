package com.blps.lab1.repositories;

import com.blps.lab1.entities.Response;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface ResponseRepository extends Repository<Response, Long> {
    void save(Response response);
    Optional<Response> findByReviewId(Long reviewId);
}
