package com.example.securitysystem.model;

import jakarta.persistence.*;

/**
 * Entity class representing an Employee in the database.
 * Maps to the "Employee" table.
 */
@Entity
@Table(name = "Employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EmployeeId")
    private Long employeeId;

    @Column(name = "FirstName")
    private String firstName;

    @Column(name = "LastName")
    private String lastName;

    @Column(name = "Email")
    private String email;

    @Column(name = "Title")
    private String title;

    @Column(name = "Password")
    private String password;

    /**
     * Determines if the employee has Manager privileges based on their title.
     *
     * @return true if the title contains "manager" (case-insensitive), false otherwise.
     */
    public boolean isManager() {
        return title != null && title.toLowerCase().contains("manager");
    }

    // --- GETTERS AND SETTERS ---
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}