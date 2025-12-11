package com.example.myapi.service;

import com.example.myapi.model.Employee;
import com.example.myapi.repository.EmployeeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Transactional
    public Employee createEmployee(Employee emp) {
        // 1. Sauvegarde initiale pour obtenir l'ID (nécessaire pour le matricule)
        Employee savedEmp = employeeRepository.save(emp);

        // 2. Génération du Matricule : 1ere lettre Prenom + 2 lettres Nom + ID
        String p1 = savedEmp.getPrenom().substring(0, 1).toUpperCase();
        String n1 = savedEmp.getNom().length() >= 2 ? savedEmp.getNom().substring(0, 2).toUpperCase() : savedEmp.getNom().toUpperCase();

        savedEmp.setMatricule(p1 + n1 + savedEmp.getId());

        // 3. Mise à jour avec le matricule
        return employeeRepository.save(savedEmp);
    }

    public Employee updateEmployee(Long id, Employee empDetails) {
        return employeeRepository.findById(id).map(emp -> {
            emp.setNom(empDetails.getNom());
            emp.setPrenom(empDetails.getPrenom());
            emp.setPoste(empDetails.getPoste());
            emp.setTauxHoraire(empDetails.getTauxHoraire());
            emp.setDateEmbauche(empDetails.getDateEmbauche());
            emp.setEmail(empDetails.getEmail());
            // On ne change généralement pas le matricule ou la date d'embauche
            return employeeRepository.save(emp);
        }).orElseThrow(() -> new RuntimeException("Employé non trouvé"));
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }
}