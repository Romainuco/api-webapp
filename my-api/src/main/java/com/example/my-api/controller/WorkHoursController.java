package com.example.myapi.controller;

import com.example.myapi.model.Employee;
import com.example.myapi.model.WorkHours;
import com.example.myapi.repository.EmployeeRepository;
import com.example.myapi.repository.WorkHoursRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/workhours")
public class WorkHoursController {

    private final WorkHoursRepository workHoursRepository;
    private final EmployeeRepository employeeRepository;

    public WorkHoursController(WorkHoursRepository workHoursRepository, EmployeeRepository employeeRepository) {
        this.workHoursRepository = workHoursRepository;
        this.employeeRepository = employeeRepository;
    }

    @Operation(summary = "Add work hours", description = "Add work hours from the database")
    @PostMapping
    @Transactional
    public ResponseEntity<?> saveWorkHours(@RequestBody WorkHours workHours) {
        System.out.println("📥 API : Réception demande sauvegarde heures...");

        // 1. VÉRIFICATION ID ET CHARGEMENT EMPLOYE (Doit exister pour la FK)
        if (workHours.getEmployee() == null || workHours.getEmployee().getId() == null) {
             System.err.println("❌ API : Employé ID manquant dans la requête.");
            return ResponseEntity.badRequest().body("L'ID de l'employé est obligatoire.");
        }
        Long empId = workHours.getEmployee().getId();
        
        // C'EST CETTE LECTURE QUI GARANTIT QUE LA LIGNE EST AJOUTÉE A LA BDD SANS PLANTER
        Optional<Employee> empOpt = employeeRepository.findById(empId);
        if (empOpt.isEmpty()) {
            System.err.println("❌ API : Employé introuvable en base ! ID=" + empId);
            return ResponseEntity.badRequest().body("Employé introuvable (ID " + empId + " n'existe pas en base).");
        }
        
        Employee emp = empOpt.get();
        workHours.setEmployee(emp); // On attache l'objet persistant

        // 2. VÉRIFICATION MINIMUM DES HEURES
        if (workHours.getPeriod() == null || workHours.getHoursWorked() == null || workHours.getDaysWorked() == null) {
            System.err.println("❌ API : Données de saisie incomplètes.");
            return ResponseEntity.badRequest().body("La période, les heures et les jours sont obligatoires.");
        }
        workHours.setPeriod(workHours.getPeriod().withDayOfMonth(1)); // Normalisation de la date

        // 3. LOGIQUE MISE A JOUR OU CRÉATION
        Optional<WorkHours> existing = workHoursRepository.findByEmployeeIdAndPeriod(
                emp.getId(), 
                workHours.getPeriod()
        );

        WorkHours saved;
        if (existing.isPresent()) {
            // Mise à jour de la ligne existante
            System.out.println("🔄 API : Mise à jour fiche existante ID " + existing.get().getId());
            WorkHours toUpdate = existing.get();
            toUpdate.setHoursWorked(workHours.getHoursWorked());
            toUpdate.setDaysWorked(workHours.getDaysWorked());
            toUpdate.setEmployee(emp);
            saved = workHoursRepository.save(toUpdate);
        } else {
            // Création de la nouvelle ligne
            System.out.println("✨ API : Création nouvelle fiche heures");
            saved = workHoursRepository.save(workHours);
        }

        System.out.println("💾 API : Sauvegarde réussie. ID = " + saved.getId());
        
        return ResponseEntity.ok(saved.getId()); 
    }

    @Operation(summary = "Get all work hours for an employee", description = "Get all work hours from the database for an employee")
    @GetMapping("/employee/{employeeId}")
    public List<WorkHours> getWorkHoursByEmployee(@PathVariable Long employeeId) {
        return workHoursRepository.findByEmployeeId(employeeId);
    }
}