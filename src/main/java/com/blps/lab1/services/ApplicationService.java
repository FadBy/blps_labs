package com.blps.lab1.services;

import com.blps.lab1.entities.Application;
import com.blps.lab1.entities.ApplicationStatus;
import com.blps.lab1.entities.Role;
import com.blps.lab1.entities.User;
import com.blps.lab1.exceptions.RequestInvalidException;
import com.blps.lab1.repositories.ApplicationRepository;
import com.blps.lab1.repositories.RoleRepository;
import com.blps.lab1.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    public Application apply(String username, String roleName) {
        Role role = roleRepository.findByName(roleName)
                .orElseThrow(() -> new RequestInvalidException("Role not found: " + roleName));
        Application app = Application.builder()
                .username(username)
                .role(role)
                .status(ApplicationStatus.NEW)
                .build();
        return applicationRepository.save(app);
    }

    public List<Application> getNewApplications() {
        return applicationRepository.findAllByStatus(ApplicationStatus.NEW);
    }

    @Transactional
    public Application approve(String username) {
        Application app = applicationRepository
                .findByUsernameAndStatus(username, ApplicationStatus.NEW)
                .orElseThrow(() -> new RequestInvalidException("No NEW application for user: " + username));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RequestInvalidException("User not found: " + username));

        user.getRoles().removeIf(r -> r.getName().equals("ROLE_USER"));
        user.getRoles().add(app.getRole());
        userRepository.save(user);

        app.setStatus(ApplicationStatus.APPROVED);
        return applicationRepository.save(app);
    }

    @Transactional
    public Application reject(String username) {
        Application app = applicationRepository
                .findByUsernameAndStatus(username, ApplicationStatus.NEW)
                .orElseThrow(() -> new IllegalStateException("No NEW application for user: " + username));
        app.setStatus(ApplicationStatus.REJECTED);
        return applicationRepository.save(app);
    }
}
