package com.example.mybatch.model;

import jakarta.persistence.*;

@Entity
@Table(name = "app_employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_sequence")
    @SequenceGenerator(name = "emp_sequence", sequenceName = "app_employee_seq", allocationSize = 1) 
    private Long id;

    private String matricule;
    private String nom;
    private String prenom;
    private String poste;
    
    @Column(name = "taux_horaire") 
    private Double tauxHoraire;
    
    private String email; 
    
    @Column(name = "date_embauche")
    private String dateEmbauche;

    public Employee() {}

    // Getters et Setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }
    public Double getTauxHoraire() { return tauxHoraire; }
    public void setTauxHoraire(Double tauxHoraire) { this.tauxHoraire = tauxHoraire; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(String dateEmbauche) { this.dateEmbauche = dateEmbauche; }
}