package com.example.securitysystem.repository;

import com.example.securitysystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * Repository interface for accessing Customer data.
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    /**
     * Finds customers whose city contains the specified string.
     *
     * @param city The substring to search for in the city field.
     * @return A list of matching customers.
     */
    List<Customer> findByCityContaining(String city);

    /**
     * Finds customers assigned to a specific Support Representative (Employee).
     *
     * @param repId The ID of the support employee.
     * @return A list of assigned customers.
     */
    List<Customer> findBySupportRepEmployeeId(Long repId);

    /**
     * Finds customers assigned to a specific Support Representative and filters by city.
     *
     * @param repId The ID of the support employee.
     * @param city  The substring to search for in the city field.
     * @return A list of matching customers.
     */
    List<Customer> findBySupportRepEmployeeIdAndCityContaining(Long repId, String city);
}