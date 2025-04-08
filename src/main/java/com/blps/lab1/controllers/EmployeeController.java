package com.blps.lab1.controllers;

import com.blps.lab1.dto.EmployeeDTO;
import com.blps.lab1.entities.Employee;
import com.blps.lab1.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping
    public void createEmployee(@RequestBody EmployeeDTO employeeDTO) {
        var employee = new Employee();
        employee.setDepartment(employeeDTO.getDepartment());
        employee.setName(employeeDTO.getName());
        employee.setPosition(employeeDTO.getPosition());
        employeeRepository.save(employee);
    }
}
