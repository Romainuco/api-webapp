package com.example.myapi.repository;

import com.example.myapi.model.WorkHours;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Repository
public interface WorkHoursRepository extends JpaRepository<WorkHours, Long> {
    // Permet de retrouver les heures d'un employé spécifique
    List<WorkHours> findByEmployeeId(Long employeeId);

    // Permet de vérifier si des heures existent déjà pour ce mois et cet employé
    Optional<WorkHours> findByEmployeeIdAndPeriod(Long employeeId, LocalDate period);
}