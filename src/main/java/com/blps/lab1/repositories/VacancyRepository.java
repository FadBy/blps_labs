package com.blps.lab1.repositories;

import com.blps.lab1.entities.Vacancy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VacancyRepository extends JpaRepository<Vacancy, Long> {
    Optional<Vacancy> findVacancyById(Long id);
}
