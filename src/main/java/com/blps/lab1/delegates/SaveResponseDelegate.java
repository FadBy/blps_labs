package com.blps.lab1.delegates;

import com.blps.lab1.entities.Response;
import com.blps.lab1.entities.Review;
import com.blps.lab1.services.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class SaveResponseDelegate implements JavaDelegate {
    private final ReviewService reviewService;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Saving response...");

        String responseJson = (String) execution.getVariable("response");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Response response = mapper.readValue(responseJson, Response.class);
        reviewService.publishResponseTransactional(response);

        log.info("Response save completed successfully");
    }
}
