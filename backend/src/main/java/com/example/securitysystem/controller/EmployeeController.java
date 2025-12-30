package com.example.securitysystem.controller;

import com.example.securitysystem.model.DTO.PasswordChangeRequest;
import com.example.securitysystem.model.Employee;
import com.example.securitysystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller responsible for Employee management operations.
 * Primarily handles password updates enforcing security policies.
 */
@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = "*")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    /**
     * Updates the password for the authenticated employee.
     * Enforces password complexity and history policies.
     *
     * @param request   The password change request object containing old and new passwords.
     * @param principal The security principal representing the currently authenticated user.
     * @return A {@link ResponseEntity} indicating the outcome:
     * <ul>
     * <li><b>200 OK</b>: Password updated successfully.</li>
     * <li><b>404 Not Found</b>: If the authenticated user cannot be found in the database.</li>
     * <li><b>400 Bad Request</b>:
     * <ul>
     * <li>If the old password is incorrect.</li>
     * <li>If the new password is the same as the old one.</li>
     * <li>If the new password does not meet complexity requirements (Regex).</li>
     * </ul>
     * </li>
     * </ul>
     */
    @PutMapping("/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequest request, Principal principal) {

        // 1. Retrieve the user initiating the request (via JWT Token)
        String email = principal.getName();
        Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);

        if (employeeOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }
        Employee employee = employeeOpt.get();

        // 2. Verification: Is the provided old password correct?
        if (!passwordEncoder.matches(request.getOldPassword(), employee.getPassword())) {
            return ResponseEntity.status(400).body("The old password is incorrect.");
        }

        // 3. Verification: Is the new password different from the old one?
        if (passwordEncoder.matches(request.getNewPassword(), employee.getPassword())) {
            return ResponseEntity.status(400).body("The new password must be different from the previous one.");
        }

        // 4. Complexity Check (Regex as per requirements)
        // At least 1 number, 1 lowercase, 1 uppercase, 1 special char, length 6-14
        String regex = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[-!$#%]).{6,14}$";
        if (!request.getNewPassword().matches(regex)) {
            return ResponseEntity.status(400).body("Password requirements: 1 Uppercase, 1 Lowercase, 1 Number, 1 Special character (-!$#%), and length 6-14.");
        }

        // 5. Success: Update and encrypt the new password
        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);

        return ResponseEntity.ok("Password updated successfully.");
    }
}