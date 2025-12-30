package com.example.securitysystem.model.DTO;

/**
 * Data Transfer Object (DTO) for handling login credentials.
 * <p>
 * This class serves purely as a data container to map the JSON request body
 * to a Java object during the authentication process. It is intentionally
 * devoid of business logic to maintain separation of concerns.
 * </p>
 */
public class LoginRequest {

    private String email;
    private String password;

    /**
     * Default constructor required for JSON deserialization (e.g., by Jackson).
     */
    public LoginRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}