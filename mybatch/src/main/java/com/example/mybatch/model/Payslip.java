package com.example.mybatch.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payslips") // Correspondance exacte avec ton API
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate period;

    // --- DONNÉES SNAPSHOT ---
    private Double monthlyWorkedHours;
    private Double snapshotTauxHoraire;
    private Double totalSalary; // Net
    private Integer daysWorked;
    private Integer mealVouchersEarned;
    private String pdfPath;

    public Payslip() {}

    // Constructeur complet pour le Batch
    public Payslip(Employee employee, LocalDate period, Double monthlyWorkedHours, 
                   Double snapshotTauxHoraire, Integer daysWorked, Double totalSalary, 
                   String pdfPath) {
        this.employee = employee;
        this.period = period;
        this.monthlyWorkedHours = monthlyWorkedHours;
        this.snapshotTauxHoraire = snapshotTauxHoraire;
        this.daysWorked = daysWorked;
        // Règle métier : 1 jour travaillé = 1 ticket resto gagné
        this.mealVouchersEarned = daysWorked; 
        this.totalSalary = totalSalary;
        this.pdfPath = pdfPath;
    }

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }

    public LocalDate getPeriod() { return period; }
    public void setPeriod(LocalDate period) { this.period = period; }

    public Double getMonthlyWorkedHours() { return monthlyWorkedHours; }
    public void setMonthlyWorkedHours(Double monthlyWorkedHours) { this.monthlyWorkedHours = monthlyWorkedHours; }

    public Double getSnapshotTauxHoraire() { return snapshotTauxHoraire; }
    public void setSnapshotTauxHoraire(Double snapshotTauxHoraire) { this.snapshotTauxHoraire = snapshotTauxHoraire; }

    public Double getTotalSalary() { return totalSalary; }
    public void setTotalSalary(Double totalSalary) { this.totalSalary = totalSalary; }

    public Integer getDaysWorked() { return daysWorked; }
    public void setDaysWorked(Integer daysWorked) { this.daysWorked = daysWorked; }

    public Integer getMealVouchersEarned() { return mealVouchersEarned; }
    public void setMealVouchersEarned(Integer mealVouchersEarned) { this.mealVouchersEarned = mealVouchersEarned; }

    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
}