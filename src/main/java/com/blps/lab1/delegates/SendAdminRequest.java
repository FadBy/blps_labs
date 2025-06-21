package com.blps.lab1.delegates;

import com.blps.lab1.security.JwtService;
import com.blps.lab1.services.ApplicationService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SendAdminRequest implements JavaDelegate {
    private final ApplicationService applicationService;

    @Override
    public void execute(DelegateExecution delegateExecution) throws Exception {
        String desiredRole = (String)delegateExecution.getVariable("role");
        String username = (String)delegateExecution.getVariable("username");
        applicationService.apply(username, desiredRole);
    }
}
