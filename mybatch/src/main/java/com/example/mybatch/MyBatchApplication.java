package com.example.mybatch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Date;

@SpringBootApplication
@EnableScheduling // Garde le scheduling actif pour le futur CRON
public class MyBatchApplication {

    private final JobLauncher jobLauncher;
    private final Job payrollJob;

    // Injecte les composants nécessaires
    public MyBatchApplication(JobLauncher jobLauncher, Job payrollJob) {
        this.jobLauncher = jobLauncher;
        this.payrollJob = payrollJob;
    }

    public static void main(String[] args) {
        SpringApplication.run(MyBatchApplication.class, args);
    }

    /**
     * Ce CommandLineRunner est un mécanisme de Lancement IMMÉDIAT pour les tests.
     * En production (quand spring.batch.job.enabled=false), seul le scheduler le lancera.
     * Pour les tests manuels, nous l'utilisons pour garantir un JobParameters unique.
     *
     * Pour utiliser ce mécanisme, assurez-vous que :
     * 1. spring.batch.job.enabled=false (dans application.properties)
     * 2. L'application est démarrée.
     * 3. Ce runner est actif.
     */
    @Bean
    public CommandLineRunner runJobImmediately() {
        return args -> {
            // NOTE: Vous pouvez désactiver ce CommandLineRunner après avoir validé
            // qu'il fonctionne, si vous préférez que seul le CRON le lance.
            
            // Le 'timestamp' rend le JobParameters unique à chaque démarrage,
            // forçant Spring Batch à considérer chaque lancement comme un nouveau travail.
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addDate("timestamp", new Date())
                    .toJobParameters();

            try {
                jobLauncher.run(payrollJob, params);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
    }
}