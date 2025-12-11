package com.example.mywebapp.model;

import org.springframework.format.annotation.DateTimeFormat; // <--- IMPORTANT
import java.time.LocalDate;

public class WorkHours {
    private Long id;
    private Employee employee; 
    
    // CORRECTION : On précise le format envoyé par le champ HTML <input type="date">
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate period;
    
    private Double hoursWorked;
    private Integer daysWorked;

    public WorkHours() {}

    // Getters et Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    
    public LocalDate getPeriod() { return period; }
    public void setPeriod(LocalDate period) { this.period = period; }
    
    public Double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(Double hoursWorked) { this.hoursWorked = hoursWorked; }
    
    public Integer getDaysWorked() { return daysWorked; }
    public void setDaysWorked(Integer daysWorked) { this.daysWorked = daysWorked; }
}