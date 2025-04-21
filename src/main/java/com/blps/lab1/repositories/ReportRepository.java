package com.blps.lab1.repositories;

import com.blps.lab1.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository extends JpaRepository<Report, Long> {
    Optional<Report> findByPeriodBetween(LocalDate startDate, LocalDate endDate);
    Optional<Report> findByPeriod(LocalDate period);
}
