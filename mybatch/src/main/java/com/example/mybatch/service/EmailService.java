package com.example.mybatch.service;

import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    // L'injection de dépendance de JavaMailSender
    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    /**
     * Envoie l'email contenant le bulletin de paie en pièce jointe.
     * @param toEmail L'adresse email du destinataire (employé).
     * @param prenom Le prénom de l'employé.
     * @param month La période du bulletin (ex: "novembre 2025").
     * @param attachmentPath Le chemin absolu du fichier PDF à attacher.
     */
    public void sendPayslipEmail(String toEmail, String prenom, String month, String attachmentPath) {
        System.out.println("📧 Tentative d'envoi de mail à : " + toEmail + " pour la période " + month);
        // --- NOUVEAU LOG DE DIAGNOSTIC ---
        System.out.println("🔍 Chemin de la pièce jointe (PDF) : " + attachmentPath);
        // ---------------------------------
        
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true for multipart message

            helper.setFrom("paie@entreprise-fictive.com");
            helper.setTo(toEmail);
            helper.setSubject("Votre bulletin de paie du mois de " + month);

            String content = String.format(
                "Bonjour %s,<br><br>" +
                "Veuillez trouver ci-joint votre bulletin de paie pour la période de **%s**.<br><br>" +
                "Cordialement,<br>" +
                "Le Service Paie", 
                prenom, month
            );
            
            helper.setText(content, true); // true pour contenu HTML

            // ----------------------------------------------------
            // GESTION DE L'ATTACHE DE FICHIER (CRITIQUE)
            // ----------------------------------------------------
            File file = new File(attachmentPath);
            
            if (file.exists() && file.canRead()) {
                FileSystemResource res = new FileSystemResource(file);
                String attachmentName = file.getName(); 
                helper.addAttachment(attachmentName, res);
                System.out.println("✅ Pièce jointe attachée : " + attachmentName);
            } else {
                // --- LOG DE DIAGNOSTIC AMÉLIORÉ ---
                System.out.println("❌ ÉCHEC ATTACHEMENT: Fichier introuvable ou illisible.");
                System.out.println("   -> File.exists() : " + file.exists());
                System.out.println("   -> File.canRead() : " + file.canRead());
                // ---------------------------------
                return; 
            }

            // --- LIGNE CRITIQUE : L'appel au serveur SMTP (MailHog) ---
            System.out.println("📬 Envoi en cours vers mailhog:1025...");
            mailSender.send(message); 
            // ----------------------------------------------------------
            
            System.out.println("✅ Email envoyé avec succès à " + toEmail);

        } catch (Exception e) {
            // Utilisation d'un log standard (ou System.out) pour les erreurs au lieu de System.err
            System.out.println("❌ ERREUR LORS DE L'ENVOI DU MAIL À " + toEmail + " : " + e.getMessage());
            e.printStackTrace();
        }
    }
}