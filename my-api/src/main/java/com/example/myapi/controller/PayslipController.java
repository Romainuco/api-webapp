package com.example.myapi.controller;

import com.example.myapi.model.Payslip;
import com.example.myapi.repository.PayslipRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/api/payslips")
@CrossOrigin(origins = "*")
public class PayslipController {

    private final PayslipRepository payslipRepository;

    public PayslipController(PayslipRepository payslipRepository) {
        this.payslipRepository = payslipRepository;
    }

    // 1. L'employé récupère TOUTES ses fiches de paie
    @Operation(summary = "Get all payslips", description = "Get all payslips from the database")
    @GetMapping("/employee/{employeeId}")
    public List<Payslip> getPayslipsByEmployee(@PathVariable Long employeeId) {
        // Il faudra ajouter cette méthode "findByEmployeeId" dans ton interface PayslipRepository
        return payslipRepository.findByEmployeeId(employeeId);
    }

    // 2. Récupérer une fiche spécifique (pour afficher le détail ou le lien PDF)
    @Operation(summary = "Get an payslip", description = "Get an payslip from the database")
    @GetMapping("/{id}")
    public ResponseEntity<Payslip> getPayslipDetail(@PathVariable Long id) {
        return payslipRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}