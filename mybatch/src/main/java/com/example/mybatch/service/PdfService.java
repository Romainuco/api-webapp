package com.example.mybatch.service;

import com.example.mybatch.model.Employee;
import com.example.mybatch.model.WorkHours;
import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.time.format.DateTimeFormatter;

@Service
public class PdfService {

    private static final String PDF_STORAGE_DIR = "/app/pdfs/";

    public String generatePayslipPdf(Employee emp, WorkHours wh) {
        try {
            // Création du dossier de stockage si inexistant
            File directory = new File(PDF_STORAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // Génère le nom de fichier
            String filename = "Bulletin_" + emp.getMatricule() + "_" + wh.getPeriod().toString() + ".pdf";
            String fullPath = PDF_STORAGE_DIR + filename;

            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(fullPath));
            document.open();

            // --- HEADER ---
            Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18);
            Paragraph title = new Paragraph("BULLETIN DE PAIE", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(new Paragraph(" ")); // Espace

            // --- INFOS PERSONNELLES ---
            PdfPTable infoTable = new PdfPTable(2);
            infoTable.setWidthPercentage(100);
            
            // Partie Gauche (Infos Entreprise)
            PdfPCell cellLeft = new PdfPCell();
            cellLeft.setBorder(Rectangle.NO_BORDER);
            cellLeft.addElement(new Paragraph("Le Bolero"));
            cellLeft.addElement(new Paragraph("38 place Mondain Chanlouineau"));
            cellLeft.addElement(new Paragraph("49000 Angers"));

            // Partie Droite (Infos Employé)
            PdfPCell cellRight = new PdfPCell();
            cellRight.setBorder(Rectangle.NO_BORDER);
            cellRight.addElement(new Paragraph("Matricule : " + emp.getMatricule()));
            cellRight.addElement(new Paragraph("Nom : " + emp.getNom().toUpperCase()));
            cellRight.addElement(new Paragraph("Prénom : " + emp.getPrenom()));
            cellRight.addElement(new Paragraph("Poste : " + emp.getPoste()));

            infoTable.addCell(cellLeft);
            infoTable.addCell(cellRight);
            document.add(infoTable);

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Période : " + wh.getPeriod().format(DateTimeFormatter.ofPattern("MM/yyyy"))));
            document.add(new Paragraph(" "));

            // --- CALCULS (Pour le PDF) ---
            double brut = wh.getHoursWorked() * emp.getTauxHoraire();
            // Sécurité pour éviter NullPointerException si daysWorked est null
            int joursTravailles = (wh.getDaysWorked() != null) ? wh.getDaysWorked() : 0;
            double ticketResto = joursTravailles * 6.0;
            double net = (brut * (1 - 0.22)) - ticketResto; // (Brut - Charges) - Tickets Resto

            // --- TABLEAU SALAIRE ---
            PdfPTable table = new PdfPTable(4); // Rubrique, Base, Taux, Montant
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 1, 1, 1});

            // En-têtes
            addHeaderCell(table, "Rubrique");
            addHeaderCell(table, "Base");
            addHeaderCell(table, "Taux");
            addHeaderCell(table, "Montant");

            // Ligne Salaire Brut
            addCell(table, "Salaire de base");
            addCell(table, String.format("%.2f h", wh.getHoursWorked()));
            addCell(table, String.format("%.2f €", emp.getTauxHoraire()));
            addCell(table, String.format("%.2f €", brut));

            // Ligne Tickets Resto (Déduction)
            addCell(table, "Tickets Restaurant");
            addCell(table, String.format("%d j", joursTravailles));
            addCell(table, "6.00 €");
            addCell(table, String.format("-%.2f €", ticketResto));

            // Ligne Net à Payer (Total)
            PdfPCell cellNetLabel = new PdfPCell(new Phrase("NET À PAYER", FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cellNetLabel.setColspan(3);
            cellNetLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellNetLabel.setPadding(5);
            table.addCell(cellNetLabel);

            PdfPCell cellNetValue = new PdfPCell(new Phrase(String.format("%.2f €", net), FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
            cellNetValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cellNetValue.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
            cellNetValue.setPadding(5);
            table.addCell(cellNetValue);

            document.add(table);
            
            // Footer
            document.add(new Paragraph(" "));
            Font small = FontFactory.getFont(FontFactory.HELVETICA, 8);
            Paragraph footer = new Paragraph("Pour faire valoir ce que de droit. Document généré par le Batch Paie.", small);
            footer.setAlignment(Element.ALIGN_CENTER);
            document.add(footer);

            document.close();
            
            // On retourne le CHEMIN ABSOLU pour l'EmailService et pour le stockage en base
            return fullPath; 

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // --- METHODES D'AIDE POUR LE TABLEAU PDF ---

    private void addHeaderCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBackgroundColor(java.awt.Color.LIGHT_GRAY);
        cell.setPadding(5);
        table.addCell(cell);
    }

    private void addCell(PdfPTable table, String text) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(5);
        table.addCell(cell);
    }
}