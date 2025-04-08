package com.blps.lab1.repositories;

import com.blps.lab1.entities.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findById(long id);
    Optional<Employee> findByName(String name);
}
