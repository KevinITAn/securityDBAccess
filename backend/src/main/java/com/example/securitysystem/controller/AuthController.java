package com.example.securitysystem.controller;

import com.example.securitysystem.model.DTO.AuthResponse;
import com.example.securitysystem.model.DTO.LoginRequest;
import com.example.securitysystem.model.Employee;
import com.example.securitysystem.repository.EmployeeRepository;
import com.example.securitysystem.security.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller responsible for handling authentication requests.
 * Provides endpoints for user login and token refreshing.
 */
@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    private final EmployeeRepository employeeRepository;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    private final JwtUtil jwtUtil;

    public AuthController(EmployeeRepository employeeRepository, JwtUtil jwtUtil) {
        this.employeeRepository = employeeRepository;
        this.jwtUtil = jwtUtil;
    }

    /**
     * Authenticates a user and generates a JWT token.
     * This is the initial entry point for accessing the system.
     *
     * @param request The login request containing email and password.
     * @return A {@link ResponseEntity} containing:
     * <ul>
     * <li><b>200 OK</b>: An {@link AuthResponse} with the JWT token, user role, and password status.</li>
     * <li><b>401 Unauthorized</b>: If the user is not found or the password is incorrect.</li>
     * </ul>
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {

        logger.info("Login attempt for email: {}", request.getEmail());

        Optional<Employee> employeeOpt = employeeRepository.findByEmail(request.getEmail());

        if (employeeOpt.isEmpty()) {
            logger.warn("Login failed: User not found for email: {}", request.getEmail());
            return ResponseEntity.status(401).body("User not found.");
        }

        Employee employee = employeeOpt.get();

        if (!passwordEncoder.matches(request.getPassword(), employee.getPassword())) {
            logger.warn("Login failed: Incorrect password for user: {}", request.getEmail());
            return ResponseEntity.status(401).body("Incorrect password.");
        }

        logger.info("Login SUCCESS for user: {} (Role: {})", request.getEmail(), employee.getTitle());

        String role = employee.isManager() ? "MANAGER" : "EMPLOYEE";
        String token = jwtUtil.generateToken(employee.getEmail(), role);
        boolean isDefaultPassword = passwordEncoder.matches("Jo5hu4!", employee.getPassword());

        return ResponseEntity.ok(new AuthResponse(token, role, isDefaultPassword));
    }

    /**
     * Refreshes the JWT token for authenticated Managers.
     * Allows Managers to extend their session beyond the default expiration time.
     *
     * @param oldTokenHeader The "Authorization" header containing the existing Bearer token.
     * @return A {@link ResponseEntity} containing:
     * <ul>
     * <li><b>200 OK</b>: A new {@link AuthResponse} with a fresh JWT token.</li>
     * <li><b>400 Bad Request</b>: If the Authorization header is missing or invalid.</li>
     * <li><b>401 Unauthorized</b>: If the token is expired, invalid, or the user cannot be found.</li>
     * <li><b>403 Forbidden</b>: If the user is not a Manager (only Managers can refresh).</li>
     * </ul>
     */
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@RequestHeader("Authorization") String oldTokenHeader) {

        String oldToken = null;
        if (oldTokenHeader != null && oldTokenHeader.startsWith("Bearer ")) {
            oldToken = oldTokenHeader.substring(7);
        } else {
            return ResponseEntity.badRequest().body("Invalid token format.");
        }

        try {
            String email = jwtUtil.getEmailFromToken(oldToken);
            Optional<Employee> employeeOpt = employeeRepository.findByEmail(email);

            if (employeeOpt.isPresent()) {
                Employee employee = employeeOpt.get();

                if (employee.isManager()) {
                    String newToken = jwtUtil.generateToken(employee.getEmail(), "MANAGER");
                    boolean isDefaultPassword = passwordEncoder.matches("Jo5hu4!", employee.getPassword());

                    return ResponseEntity.ok(new AuthResponse(newToken, "MANAGER", isDefaultPassword));
                } else {
                    return ResponseEntity.status(403).body("Only Managers can perform automatic token refresh.");
                }
            }
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Token expired or invalid.");
        }

        return ResponseEntity.status(401).body("Unable to refresh token.");
    }
}