package com.blps.lab1.repositories;

import com.blps.lab1.entities.Application;
import com.blps.lab1.entities.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    List<Application> findAllByStatus(ApplicationStatus status);
    Optional<Application> findByUsernameAndStatus(String username, ApplicationStatus status);
}
