package com.example.mywebapp.proxy;

import com.example.mywebapp.model.Employee;
import com.example.mywebapp.model.Payslip;
import com.example.mywebapp.model.WorkHours;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Vérifie que l'URL correspond bien au port de ton API
@FeignClient(name = "my-api", url = "${my.api.url}")
public interface ApiProxy {

    // ==========================================
    //                 EMPLOYE
    // ==========================================
    
    @GetMapping("/api/employee")
    List<Employee> getAllEmployees();

    @GetMapping("/api/employee/{id}")
    Employee getEmployeeById(@PathVariable("id") Long id);

    @PostMapping("/api/employee")
    Employee createEmployee(@RequestBody Employee e);

    @PutMapping("/api/employee/{id}")
    Employee updateEmployee(@PathVariable("id") Long id, @RequestBody Employee e);

    @DeleteMapping("/api/employee/{id}")
    void deleteEmployee(@PathVariable("id") Long id);


    // ==========================================
    //              FICHES DE PAIE 
    // ==========================================
    @GetMapping("/api/payslips/employee/{employeeId}")
    List<Payslip> getPayslips(@PathVariable("employeeId") Long employeeId);

    // ==========================================
    //        GESTION DES HEURES (RH)
    // ==========================================

    @PostMapping("/api/workhours")
    void saveWorkHours(@RequestBody WorkHours workHours);

    // --- C'EST CETTE MÉTHODE QU'IL MANQUAIT ! ---
    @GetMapping("/api/workhours/employee/{employeeId}")
    List<WorkHours> getWorkHoursByEmployee(@PathVariable("employeeId") Long employeeId);
}