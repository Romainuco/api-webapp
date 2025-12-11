package com.example.mybatch.config;

import com.example.mybatch.model.Payslip;
import com.example.mybatch.model.WorkHours;
import com.example.mybatch.repository.PayslipRepository;
import com.example.mybatch.service.EmailService;
import com.example.mybatch.service.PdfService;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.batch.item.database.builder.JpaItemWriterBuilder;
import org.springframework.batch.item.database.builder.JpaPagingItemReaderBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Configuration
public class PayrollBatchConfig {

    private final PdfService pdfService;
    private final EmailService emailService;
    private final DataSource dataSource;
    private final PayslipRepository payslipRepository; // <--- INJECTION NÉCESSAIRE

    // Mise à jour du constructeur pour tout injecter
    public PayrollBatchConfig(PdfService pdfService, EmailService emailService, DataSource dataSource, PayslipRepository payslipRepository) {
        this.pdfService = pdfService;
        this.emailService = emailService;
        this.dataSource = dataSource;
        this.payslipRepository = payslipRepository;
    }

    // 1. READER
    @Bean
    public JpaPagingItemReader<WorkHours> reader(EntityManagerFactory entityManagerFactory) {
        return new JpaPagingItemReaderBuilder<WorkHours>()
                .name("workHoursReader")
                .entityManagerFactory(entityManagerFactory)
                .queryString("SELECT w FROM WorkHours w ORDER BY w.id ASC")
                .pageSize(10)
                .build();
    }

    // 2. PROCESSOR (Le cœur de la logique)
    @Bean
    public ItemProcessor<WorkHours, Payslip> processor() {
        return workHours -> {
            if (workHours.getEmployee() == null) return null;

            LocalDate today = LocalDate.now();
            LocalDate workDate = workHours.getPeriod();
            
            // ----------------------------------------------------
            // 1. FILTRES & VÉRIFICATIONS (Logique Métier)
            // ----------------------------------------------------

            // RÈGLE 1 : Ignorer si mois en cours (On ne traite que le passé)
            if (!workDate.isBefore(today.withDayOfMonth(1))) {
                System.out.println("⏳ Ignoré (Mois non terminé) : " + workDate);
                return null;
            }
            
            // RÈGLE 2 : Vérification des Doublons (Si déjà un bulletin)
            Optional<Payslip> existingPayslip = payslipRepository.findByEmployeeIdAndPeriod(
                workHours.getEmployee().getId(), 
                workDate
            );
            
            if (existingPayslip.isPresent()) {
                System.out.println("🛑 Ignoré (Doublon) : Bulletin déjà généré pour " + workDate);
                return null; 
            }

            // ----------------------------------------------------
            // 2. CALCULS & ACTIONS (Si le bulletin est NOUVEAU et PASSÉ)
            // ----------------------------------------------------
            System.out.println("✅ Génération Bulletin : " + workHours.getEmployee().getNom() + " - " + workDate);

            Double taux = workHours.getEmployee().getTauxHoraire() != null ? workHours.getEmployee().getTauxHoraire() : 0.0;
            Double h = workHours.getHoursWorked() != null ? workHours.getHoursWorked() : 0.0;
            Integer j = workHours.getDaysWorked() != null ? workHours.getDaysWorked() : 0;
            
            double brut = h * taux;
            double tickets = j * 6.0;
            double net = (brut * (1 - 0.22)) - tickets; // Formule de paie

            // 2a. Générer le PDF
            String pdfPath = pdfService.generatePayslipPdf(workHours.getEmployee(), workHours);

            // 2b. ENVOYER L'EMAIL
            if (pdfPath != null) {
                String moisStr = workDate.format(DateTimeFormatter.ofPattern("MMMM yyyy"));
                emailService.sendPayslipEmail(
                    workHours.getEmployee().getEmail(), 
                    workHours.getEmployee().getPrenom(),
                    moisStr,
                    pdfPath // Le chemin absolu pour l'attachement
                );
            }

            // 2c. Créer l'objet Payslip à sauvegarder en BDD
            return new Payslip(workHours.getEmployee(), workDate, h, taux, j, net, pdfPath);
        };
    }

    // 3. WRITER (Reste inchangé)
    @Bean
    public JpaItemWriter<Payslip> writer(EntityManagerFactory entityManagerFactory) {
        return new JpaItemWriterBuilder<Payslip>()
                .entityManagerFactory(entityManagerFactory)
                .build();
    }

    // 4. STEP (Reste inchangé)
    @Bean
    public Step step1(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                      JpaPagingItemReader<WorkHours> reader,
                      ItemProcessor<WorkHours, Payslip> processor,
                      JpaItemWriter<Payslip> writer) {
        return new StepBuilder("generatePayslipsStep", jobRepository)
                .<WorkHours, Payslip>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }

    // 5. JOB (Reste inchangé)
    @Bean
    public Job payrollJob(JobRepository jobRepository, Step step1) {
        return new JobBuilder("payrollJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(step1)
                .build();
    }
}