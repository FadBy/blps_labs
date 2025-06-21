package com.blps.lab1.delegates;

import com.blps.lab1.entities.User;
import com.blps.lab1.repositories.RoleRepository;
import com.blps.lab1.repositories.UserRepository;
import com.blps.lab1.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class AddUserToDatabase implements JavaDelegate {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;

    @Override
    public void execute(DelegateExecution execution) {
        var username = execution.getVariable("username");
        var password = execution.getVariable("password");
        var role = execution.getVariable("role");

        var user = userRepository.save(User.builder()
                        .username((String)username)
                        .password((String)password)
                        .roles(List.of(roleRepository.findByName((String)role).get()))
                .build());

        String jwtToken = jwtService.generateToken(user);
        execution.setVariable("token", jwtToken);
    }
}
