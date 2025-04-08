package com.blps.lab1.repositories;

import com.blps.lab1.entities.Report;
import org.springframework.data.repository.Repository;

import java.time.LocalDate;
import java.util.Optional;

public interface ReportRepository extends Repository<Report, Long> {
    Report save(Report report);
    Optional<Report> findByPeriodBetween(LocalDate startDate, LocalDate endDate);
    Optional<Report> findByPeriod(LocalDate period);
}
