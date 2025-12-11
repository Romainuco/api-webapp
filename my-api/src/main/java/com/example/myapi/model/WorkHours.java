package com.example.myapi.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "paie_work_hours", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"employee_id", "period"})
})
public class WorkHours {

    @Id
    // --- CHANGEMENT CRUCIAL : Passage en mode SEQUENCE ---
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wh_sequence")
    @SequenceGenerator(name = "wh_sequence", sequenceName = "paie_work_hours_seq", allocationSize = 1)
    private Long id;
    // ----------------------------------------------------

    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate period;

    @Column(nullable = false)
    private Double hoursWorked;

    @Column(nullable = false)
    private Integer daysWorked;

    public WorkHours() {}

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