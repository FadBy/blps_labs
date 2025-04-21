package com.blps.lab1.controllers;

import com.blps.lab1.dto.VacancyDTO;
import com.blps.lab1.entities.Vacancy;
import com.blps.lab1.repositories.VacancyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/vacancy")
public class VacancyController {
    private final VacancyRepository vacancyRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_VACANCY')")
    public void createVacancy(@RequestBody VacancyDTO vacancyDTO) {
        var vacancy = new Vacancy();
        vacancy.setTitle(vacancyDTO.getTitle());
        vacancy.setDescription(vacancyDTO.getDescription());
        vacancyRepository.save(vacancy);
    }
}
