package com.example.mywebapp.model;

import java.time.LocalDate;

public class Payslip {
    private Long id;
    private LocalDate period;
    private Double totalSalary;
    private String pdfPath;
    private Integer daysWorked;

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDate getPeriod() { return period; }
    public void setPeriod(LocalDate period) { this.period = period; }
    public Double getTotalSalary() { return totalSalary; }
    public void setTotalSalary(Double totalSalary) { this.totalSalary = totalSalary; }
    public String getPdfPath() { return pdfPath; }
    public void setPdfPath(String pdfPath) { this.pdfPath = pdfPath; }
    public Integer getDaysWorked() { return daysWorked; }
    public void setDaysWorked(Integer daysWorked) { this.daysWorked = daysWorked; }
}