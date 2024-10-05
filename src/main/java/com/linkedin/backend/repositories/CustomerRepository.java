package com.linkedin.backend.repositories;

import com.linkedin.backend.entities.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer,Integer> {
    Optional<Customer> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsById(Integer id);
}
