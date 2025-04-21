package com.blps.lab1.controllers;

import com.blps.lab1.entities.Report;
import com.blps.lab1.exceptions.ErrorMessage;
import com.blps.lab1.exceptions.ReportNotFoundException;
import com.blps.lab1.services.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/report")
public class ReportController {
    private final ReportService reportService;

    @GetMapping
    @PreAuthorize("hasAuthority('RECEIVE_REPORT')")
    public Report getWeeklyReport(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return reportService.findReportByPeriod(date);
    }

    @ExceptionHandler(ReportNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage handleReportNotFound(ReportNotFoundException ex) {
        return new ErrorMessage(ex.getMessage());
    }
}
