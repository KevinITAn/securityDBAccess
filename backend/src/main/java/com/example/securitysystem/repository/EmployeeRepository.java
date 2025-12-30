package com.example.securitysystem.repository;

import com.example.securitysystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository interface for accessing Employee data.
 */
@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    /**
     * Finds an employee by their email address.
     *
     * @param email The email to search for.
     * @return An Optional containing the employee if found.
     */
    Optional<Employee> findByEmail(String email);
}