package com.blps.lab1.delegates;

import com.blps.lab1.entities.Report;
import com.blps.lab1.entities.Response;
import com.blps.lab1.entities.Review;
import com.blps.lab1.repositories.ReportRepository;
import com.blps.lab1.services.ReportService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveReportDelegate implements JavaDelegate {
    private final ReportRepository reportRepository;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Saving report...");

        String reportJson = (String) execution.getVariable("report");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Report report = mapper.readValue(reportJson, Report.class);

        reportRepository.save(report);

        log.info("Report save completed successfully");
    }
}
