package com.example.mywebapp.model;

import java.time.LocalDate;

// Classe simple sans @Entity car pas de BDD ici
public class Employee {
    private Long id;
    private String matricule;
    private String nom;
    private String prenom;
    private LocalDate dateEmbauche;
    private String poste;
    private Double tauxHoraire;
    private String email;

    // Getters et Setters (Génère-les ou utilise Lombok @Data)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMatricule() { return matricule; }
    public void setMatricule(String matricule) { this.matricule = matricule; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }
    public LocalDate getDateEmbauche() { return dateEmbauche; }
    public void setDateEmbauche(LocalDate dateEmbauche) { this.dateEmbauche = dateEmbauche; }
    public String getPoste() { return poste; }
    public void setPoste(String poste) { this.poste = poste; }
    public Double getTauxHoraire() { return tauxHoraire; }
    public void setTauxHoraire(Double tauxHoraire) { this.tauxHoraire = tauxHoraire; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}