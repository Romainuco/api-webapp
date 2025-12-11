package com.example.myapi.repository;

import com.example.myapi.model.Payslip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PayslipRepository extends JpaRepository<Payslip, Long> {

    // 1. Pour l'API (Controller) : Récupérer toutes les fiches d'un employé
    // Spring comprend automatiquement qu'il doit chercher dans le champ "employee" puis son "id"
    List<Payslip> findByEmployeeId(Long employeeId);

    // 2. Pour le Batch (Futur) : Vérifier si une fiche existe déjà pour ce mois
    // Utile pour éviter les doublons si on relance le batch par erreur
    Optional<Payslip> findByEmployeeIdAndPeriod(Long employeeId, LocalDate period);
}