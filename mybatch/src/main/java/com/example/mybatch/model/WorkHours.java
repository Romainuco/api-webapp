package com.example.mybatch.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "paie_work_hours")
public class WorkHours {
    
    @Id
    // --- Changement vers SEQUENCE ---
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wh_sequence")
    @SequenceGenerator(name = "wh_sequence", sequenceName = "paie_work_hours_seq", allocationSize = 1) 
    private Long id;
    // --------------------------------

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    private LocalDate period;
    private Double hoursWorked;
    private Integer daysWorked;

    public WorkHours() {}

    // Getters
    public Long getId() { return id; }
    public Employee getEmployee() { return employee; }
    public LocalDate getPeriod() { return period; }
    public Double getHoursWorked() { return hoursWorked; }
    public Integer getDaysWorked() { return daysWorked; }
}