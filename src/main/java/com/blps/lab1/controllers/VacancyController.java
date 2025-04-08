package com.blps.lab1.controllers;

import com.blps.lab1.dto.VacancyDTO;
import com.blps.lab1.entities.Vacancy;
import com.blps.lab1.repositories.VacancyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/vacancy")
public class VacancyController {
    @Autowired
    private VacancyRepository vacancyRepository;

    @PostMapping
    public void createVacancy(@RequestBody VacancyDTO vacancyDTO) {
        var vacancy = new Vacancy();
        vacancy.setTitle(vacancyDTO.getTitle());
        vacancy.setDescription(vacancyDTO.getDescription());
        vacancyRepository.save(vacancy);
    }
}
