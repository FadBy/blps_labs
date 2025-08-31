package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.services.TemplateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CreatePositiveReviewTemplateDelegate implements JavaDelegate {
    private final TemplateService templateService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Creating positive review template...");

        String message = templateService.createResponseTemplate(Double.parseDouble((String) execution.getVariable("rating")));

        execution.setVariable("message", message);

        log.info("Created positive review template successfully");
    }
}
