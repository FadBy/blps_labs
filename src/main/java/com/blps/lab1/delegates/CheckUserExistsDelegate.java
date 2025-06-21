package com.blps.lab1.delegates;

import com.blps.lab1.entities.User;
import com.blps.lab1.exceptions.RequestInvalidException;
import com.blps.lab1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CheckUserExistsDelegate implements JavaDelegate {

    private final UserRepository userRepository;

    @Override
    public void execute(DelegateExecution execution) {
        String username = (String) execution.getVariable("username");

        boolean userExists = userRepository.findByUsername(username).isPresent();
        execution.setVariable("userExists", userExists);
    }
}

