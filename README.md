# Gestion de Paie et Employés

Ce projet est une application complète de gestion des employés et d'automatisation des fiches de paie, construite avec une architecture microservices (Spring Boot & Docker).

## 🚀 Fonctionnalités Principales

- **Gestion des Employés** : Ajout, modification et suppression des employés (Interface RH).
- **Gestion des Heures** : Saisie des heures travaillées par mois pour chaque employé.
- **Génération Automatique des Fiches de Paie** :
    - Un **Batch** automatique s'exécute le **1er de chaque mois à 01h00 du matin**.
    - Il génère les fiches de paie au format PDF.
    - Il envoie les fiches de paie par email (via SMTP Gmail).
    - **Activation Manuelle** : Pour obtenir les PDF immédiatement sans attendre la date plannifiée, faites un restart du service `mybatch` depuis le shell.
- **Consultation** :
    - Les employés peuvent consulter leurs propres fiches de paie.
    - Les RH ont accès à un tableau de bord complet.

## 🛠️ Architecture Technique

Le projet se compose de plusieurs services orchestrés via Docker Compose :
- **my-api** (Port 8080) : Le Backend REST.
- **mywebapp** (Port 8081) : Le Frontend (Thymeleaf).
- **mybatch** : Le service de génération de paie (Tâche planifiée).
- **db** (Port 5432) : Base de données PostgreSQL.

---

## � Prérequis

Avant de lancer le projet, assurez-vous d'avoir installé :

- **Docker Desktop** (avec Docker Compose).
- *Optionnel* : Un client API comme Postman ou cURL (pour utiliser le Quick Start).

## 🚀 Installation & Démarrage

1. Ouvrez un terminal à la racine du projet.
2. Lancez l'environnement complet avec Docker Compose :
   ```bash
   docker-compose up -d --build
   ```
3. Une fois les conteneurs démarrés, accédez à l'application web : **[http://localhost:8081](http://localhost:8081)**

---

## ⚡ Mini Base de Données (Quick Start)

Une fois l'application lancée, vous pouvez utiliser les commandes ci-dessous dans votre terminal (Git Bash, PowerShell ou Linux) pour injecter directement des utilisateurs et tester l'application sans passer par l'Interface RH.

### 1. Créer un Responsable RH
*Cet utilisateur aura accès au Dashboard de gestion.*

```bash
curl -X POST http://localhost:8080/api/employee \
-H "Content-Type: application/json" \
-d '{
    "matricule": "RH01",
    "nom": "Boss",
    "prenom": "Admin",
    "email": "rh@company.com",
    "poste": "Responsable RH",
    "dateEmbauche": "2025-11-01",
    "tauxHoraire": 30.0
}'
```

👉 **Connexion avec :** Matricule `RH01` / Mot de passe `Admin`

### 2. Créer un Employé Standard
*Cet utilisateur aura accès uniquement à son historique de fiches de paie.*

```bash
curl -X POST http://localhost:8080/api/employee \
-H "Content-Type: application/json" \
-d '{
    "matricule": "EMP01",
    "nom": "Dev",
    "prenom": "John",
    "email": "john.dev@company.com",
    "poste": "Développeur Fullstack",
    "dateEmbauche": "2025-12-15",
    "tauxHoraire": 20.0
}'
```

👉 **Connexion avec :** Matricule `EMP01` / Mot de passe `John`

---

## 🧪 Test du Batch de Paie (Génération PDF)

Le batch de génération des fiches de paie est configuré pour s'exécuter automatiquement le **1er de chaque mois à 01h00**.

Pour **forcer la génération des PDF immédiatement** (sans attendre la date planifiée), il suffit de redémarrer le conteneur du batch :

```bash
docker-compose restart mybatch
```

Cela déclenchera instantanément le traitement pour tous les employés présents dans la base de données.

---

## 🔐 Authentification

L'application utilise un système de connexion simplifié pour la démonstration :
- **Identifiant** : Le `Matricule` de l'employé.
- **Mot de passe** : Le `Prénom` de l'employé (insensible à la casse).

La redirection est automatique selon le poste :
- Si le poste contient **"RH"** -> Redirection vers le **Dashboard RH**.
- Sinon -> Redirection vers l'**Espace Employé**.
