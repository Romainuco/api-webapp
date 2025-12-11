package com.example.myapi.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "payslips")
public class Payslip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // L'employé à qui appartient cette fiche
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // La période concernée (ex: 2025-11-01)
    @Column(nullable = false)
    private LocalDate period;

    // --- DONNÉES SNAPSHOT (Figées au moment du calcul du batch) ---

    // Heures et Salaire
    private Double monthlyWorkedHours;
    private Double snapshotTauxHoraire;
    private Double totalSalary;

    // Jours et Tickets Resto (Pour ton bonus)
    private Integer daysWorked;
    private Integer mealVouchersEarned;

    // Lien vers le fichier PDF généré
    private String pdfPath;

    // Constructeur vide
    public Payslip() {}

    // --- GETTERS ET SETTERS ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public LocalDate getPeriod() {
        return period;
    }

    public void setPeriod(LocalDate period) {
        this.period = period;
    }

    public Double getMonthlyWorkedHours() {
        return monthlyWorkedHours;
    }

    public void setMonthlyWorkedHours(Double monthlyWorkedHours) {
        this.monthlyWorkedHours = monthlyWorkedHours;
    }

    public Double getSnapshotTauxHoraire() {
        return snapshotTauxHoraire;
    }

    public void setSnapshotTauxHoraire(Double snapshotTauxHoraire) {
        this.snapshotTauxHoraire = snapshotTauxHoraire;
    }

    public Double getTotalSalary() {
        return totalSalary;
    }

    public void setTotalSalary(Double totalSalary) {
        this.totalSalary = totalSalary;
    }

    public Integer getDaysWorked() {
        return daysWorked;
    }

    public void setDaysWorked(Integer daysWorked) {
        this.daysWorked = daysWorked;
    }

    public Integer getMealVouchersEarned() {
        return mealVouchersEarned;
    }

    public void setMealVouchersEarned(Integer mealVouchersEarned) {
        this.mealVouchersEarned = mealVouchersEarned;
    }

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }
}