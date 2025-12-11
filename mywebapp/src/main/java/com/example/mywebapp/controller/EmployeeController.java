package com.example.mywebapp.controller;

import com.example.mywebapp.model.Employee;
import com.example.mywebapp.model.Payslip; // Assure-toi d'avoir cet import
import com.example.mywebapp.model.WorkHours;
import com.example.mywebapp.proxy.ApiProxy;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
public class EmployeeController {

    private final ApiProxy apiProxy;

    public EmployeeController(ApiProxy apiProxy) {
        this.apiProxy = apiProxy;
    }

    // ==========================================
    //                  LOGIN
    // ==========================================
    @GetMapping("/")
    public String loginPage() { return "login-employee"; }

    @PostMapping("/login")
    public String doLogin(@RequestParam String matricule, @RequestParam String password, HttpSession session, Model model) {
        List<Employee> employees = apiProxy.getAllEmployees();
        
        Employee found = employees.stream()
                .filter(e -> e.getMatricule() != null && e.getMatricule().equalsIgnoreCase(matricule))
                .findFirst().orElse(null);

        if (found != null && found.getPrenom().equalsIgnoreCase(password)) {
            session.setAttribute("user", found);
            if (found.getPoste() != null && found.getPoste().toUpperCase().contains("RH")) {
                return "redirect:/rh-dashboard";
            } else {
                return "redirect:/employe/payslip";
            }
        }
        
        model.addAttribute("error", "Identifiants incorrects");
        return "login-employee";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }

    // ==========================================
    //              DASHBOARD RH
    // ==========================================
    @GetMapping("/rh-dashboard")
    public String rhDashboard(Model model, HttpSession session) {
        Employee user = (Employee) session.getAttribute("user");
        if (user == null) return "redirect:/";
        
        // 1. Liste de tous les employés (pour la gestion)
        model.addAttribute("employees", apiProxy.getAllEmployees());
        
        // 2. L'utilisateur connecté
        model.addAttribute("user", user);

        // 3. LA CORRECTION CRUCIALE : Charger les bulletins du RH lui-même
        List<Payslip> mesBulletins = apiProxy.getPayslips(user.getId());
        model.addAttribute("myPayslips", mesBulletins); // <--- C'EST CA QUI MANQUAIT !

        return "payslip";
    }
    
    // CONSULTATION DÉTAILLÉE (L'OEIL)
    @GetMapping("/rh-employe/{id}")
    public String viewEmployeeByRh(@PathVariable Long id, Model model) {
        Employee emp = apiProxy.getEmployeeById(id);
        model.addAttribute("employee", emp);
        // On charge aussi les bulletins de cet employé
        model.addAttribute("payslips", apiProxy.getPayslips(id));
        return "rh-employee-dashboard"; 
    }

    // ==========================================
    //      GESTION DES HEURES
    // ==========================================

    @GetMapping("/rh/hours")
    public String showWorkHoursForm(@RequestParam(required = false) Long employeeId, 
                                    @RequestParam(required = false) LocalDate date,
                                    Model model) {
        model.addAttribute("employees", apiProxy.getAllEmployees());
        WorkHours wh = new WorkHours();
        
        if (employeeId != null) {
            Employee selectedEmp = apiProxy.getEmployeeById(employeeId);
            wh.setEmployee(selectedEmp);
            if (date != null) wh.setPeriod(date);

            List<WorkHours> existingHistory = apiProxy.getWorkHoursByEmployee(employeeId);
            List<WorkHours> fullHistory = new ArrayList<>();

            LocalDate iterator = LocalDate.now().withDayOfMonth(1);
            LocalDate stopDate = (selectedEmp.getDateEmbauche() != null) ? selectedEmp.getDateEmbauche().withDayOfMonth(1) : iterator.minusMonths(6);
            if (stopDate.isAfter(iterator)) stopDate = iterator; 

            while (!iterator.isBefore(stopDate)) {
                LocalDate currentMonth = iterator;
                WorkHours found = existingHistory.stream()
                        .filter(w -> w.getPeriod().withDayOfMonth(1).isEqual(currentMonth))
                        .findFirst().orElse(null);

                if (found != null) {
                    fullHistory.add(found);
                } else {
                    WorkHours missing = new WorkHours();
                    missing.setPeriod(currentMonth);
                    missing.setEmployee(selectedEmp);
                    fullHistory.add(missing);
                }
                iterator = iterator.minusMonths(1);
            }
            model.addAttribute("history", fullHistory);
            model.addAttribute("selectedEmployee", selectedEmp);
        }
        model.addAttribute("workHours", wh);
        return "add-workhours";
    }

    @PostMapping("/rh/hours")
    public String saveWorkHours(@ModelAttribute WorkHours workHours, @RequestParam Long employeeId, RedirectAttributes redirectAttributes) {
        Employee emp = new Employee();
        emp.setId(employeeId);
        workHours.setEmployee(emp);
        apiProxy.saveWorkHours(workHours);
        redirectAttributes.addFlashAttribute("success", "Heures enregistrées avec succès !");
        return "redirect:/rh/hours?employeeId=" + employeeId;
    }

    // ==========================================
    //              CRUD EMPLOYÉS
    // ==========================================
    @GetMapping("/rh/add")
    public String showAddForm(Model model) {
        model.addAttribute("employeeForm", new Employee());
        return "add-employee";
    }

    @PostMapping("/rh/add")
    public String processAdd(@ModelAttribute Employee employee) {
        apiProxy.createEmployee(employee);
        return "redirect:/rh-dashboard";
    }

    @GetMapping("/rh/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("employeeForm", apiProxy.getEmployeeById(id));
        return "edit-employee";
    }

    @PostMapping("/rh/update/{id}")
    public String processUpdate(@PathVariable Long id, @ModelAttribute Employee employee) {
        apiProxy.updateEmployee(id, employee);
        return "redirect:/rh-dashboard";
    }

    @GetMapping("/rh/delete/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        apiProxy.deleteEmployee(id);
        return "redirect:/rh-dashboard";
    }

    // ==========================================
    //              ESPACE EMPLOYÉ
    // ==========================================
    @GetMapping("/employe/payslip")
    public String employeDashboard(Model model, HttpSession session) {
        Employee user = (Employee) session.getAttribute("user");
        if (user == null) return "redirect:/";
        model.addAttribute("user", user);
        model.addAttribute("myPayslips", apiProxy.getPayslips(user.getId()));
        return "employee-payslip";
    }
}