package com.blps.lab1.controllers;

import com.blps.lab1.dto.EmployeeDTO;
import com.blps.lab1.entities.Employee;
import com.blps.lab1.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeRepository employeeRepository;

    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_EMPLOYEE')")
    public void createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        var employee = new Employee();
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setName(employeeDTO.getName());
        employee.setPosition(employeeDTO.getPosition());
        employeeRepository.save(employee);
    }
}
