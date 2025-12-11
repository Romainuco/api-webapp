package com.example.mybatch.repository;

import com.example.mybatch.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {
    
    // Méthode pour vérifier si une fiche de paie existe déjà pour un mois donné
    Optional<Payslip> findByEmployeeIdAndPeriod(Long employeeId, LocalDate period);

    // Méthode pour récupérer les fiches par employé
    List<Payslip> findByEmployeeId(Long employeeId);
}