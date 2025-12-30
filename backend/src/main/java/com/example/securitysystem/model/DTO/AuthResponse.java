package com.example.securitysystem.model.DTO;

/**
 * Data Transfer Object (DTO) representing the authentication response.
 * <p>
 * This class is designed to transport the generated JWT token and user status details
 * from the backend to the client after a successful login. It contains no behavior,
 * only data fields required by the frontend application.
 * </p>
 */
public class AuthResponse {

    private String token;
    private String role;
    private boolean requiresPasswordChange;

    /**
     * Constructs a new AuthResponse with the specified token and user details.
     *
     * @param token                  The JWT token string.
     * @param role                   The user's role (e.g., MANAGER, EMPLOYEE).
     * @param requiresPasswordChange True if the user is using a default password.
     */
    public AuthResponse(String token, String role, boolean requiresPasswordChange) {
        this.token = token;
        this.role = role;
        this.requiresPasswordChange = requiresPasswordChange;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public boolean isRequiresPasswordChange() {
        return requiresPasswordChange;
    }

    public void setRequiresPasswordChange(boolean requiresPasswordChange) {
        this.requiresPasswordChange = requiresPasswordChange;
    }
}