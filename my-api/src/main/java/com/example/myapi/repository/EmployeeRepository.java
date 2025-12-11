package com.example.myapi.repository;

import com.example.myapi.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    // Spring Data JPA génère automatiquement les méthodes findById, save, delete, etc.
    boolean existsByEmail(String email);

    Optional<Employee> findByMatricule(String matricule);

}
