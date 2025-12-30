package com.example.securitysystem.model.DTO;

/**
 * Data Transfer Object (DTO) for password change requests.
 * <p>
 * This POJO (Plain Old Java Object) acts as a carrier for the data required
 * to update a user's password. It isolates the internal database entities
 * from the external API layer, ensuring that only necessary data is exposed.
 * </p>
 */
public class PasswordChangeRequest {

    private String oldPassword;
    private String newPassword;
    private String confirmPassword;

    public PasswordChangeRequest() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}