package com.blps.lab1.delegates;

import com.blps.lab1.entities.Review;
import com.blps.lab1.entities.Response;
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
public class SendNotificationDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        log.info("Sending notification...");
        
        // Получаем данные из переменных процесса
        String reviewJson = (String) execution.getVariable("review");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        Review review = mapper.readValue(reviewJson, Review.class);

        String responseJson = (String) execution.getVariable("response");

        Response response = mapper.readValue(responseJson, Response.class);
        
        if (review == null) {
            throw new RuntimeException("Review not found in process variables");
        }
        
        // Определяем тип уведомления на основе текущей задачи
        String currentTask = execution.getCurrentActivityId();
        String notificationType = determineNotificationType(currentTask);
        
        log.info("Sending {} notification for review ID: {}", notificationType, review.getId());
        
        // Логируем отправку уведомления (в реальном приложении здесь была бы отправка email/SMS)
        log.info("Notification sent: {} for review ID: {} from employee: {}", 
                notificationType, review.getId(), review.getEmployee().getName());
        
        // Устанавливаем переменные процесса
        execution.setVariable("notificationSent", true);
        execution.setVariable("notificationType", notificationType);
        execution.setVariable("notificationTime", java.time.LocalDateTime.now().toString());
        
        log.info("Notification sent successfully");
    }
    
    private String determineNotificationType(String taskId) {
        switch (taskId) {
            case "Event_0a6q6fp": // Отправляем подготовленный ответ
                return "RESPONSE_READY";
            case "Event_0wq4mjy": // Отправляем отчет на анализ HR
                return "ANALYTICS_REPORT_READY";
            default:
                return "GENERAL_NOTIFICATION";
        }
    }
}
