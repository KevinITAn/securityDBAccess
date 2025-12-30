package com.example.securitysystem.controller;

import com.example.securitysystem.model.Customer;
import com.example.securitysystem.model.Employee;
import com.example.securitysystem.repository.CustomerRepository;
import com.example.securitysystem.repository.EmployeeRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

/**
 * Controller responsible for managing Customer data.
 * Access to data is filtered based on the role of the authenticated user (Manager vs Employee).
 */
@RestController
@RequestMapping("/api/customers")
@CrossOrigin(origins = "*")
public class CustomerController {

    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;

    public CustomerController(CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
    }

    /**
     * Retrieves a list of customers.
     * <ul>
     * <li><b>Managers</b> can see all customers or filter by city.</li>
     * <li><b>Employees</b> can only see customers assigned to them (SupportRep), optionally filtered by city.</li>
     * </ul>
     *
     * @param city      (Optional) The city to filter customers by.
     * @param principal The security principal representing the currently authenticated user.
     * @return A {@link ResponseEntity} containing:
     * <ul>
     * <li><b>200 OK</b>: A list of {@link Customer} objects matching the criteria.</li>
     * <li><b>500 Internal Server Error</b>: If the authenticated user record is not found (RuntimeException).</li>
     * </ul>
     */
    @GetMapping
    public ResponseEntity<List<Customer>> getCustomers(
            @RequestParam(required = false) String city,
            Principal principal) {

        String email = principal.getName();
        // Use .orElseThrow() to safely unwrap the Optional
        Employee me = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Customer> customers;

        if (me.isManager()) {
            // MANAGER LOGIC
            if (city != null) {
                customers = customerRepository.findByCityContaining(city);
            } else {
                customers = customerRepository.findAll();
            }
        } else {
            // EMPLOYEE LOGIC
            if (city != null) {
                customers = customerRepository.findBySupportRepEmployeeIdAndCityContaining(me.getEmployeeId(), city);
            } else {
                customers = customerRepository.findBySupportRepEmployeeId(me.getEmployeeId());
            }
        }

        return ResponseEntity.ok(customers);
    }
}