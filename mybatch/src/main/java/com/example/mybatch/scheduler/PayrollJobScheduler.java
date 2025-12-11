package com.example.mybatch.scheduler;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class PayrollJobScheduler {

    private final JobLauncher jobLauncher;
    private final Job payrollJob;

    public PayrollJobScheduler(JobLauncher jobLauncher, Job payrollJob) {
        this.jobLauncher = jobLauncher;
        this.payrollJob = payrollJob;
    }

    // Le CRON mensuel (le 1er du mois à 1h du matin)
    // NOTE: Ce code ne sera exécuté que si le batch est UP et que l'heure arrive.
    // Il utilise le même mécanisme de paramètres uniques que le CommandLineRunner.
    @Scheduled(cron = "0 0 1 1 * ?", zone = "Europe/Paris")
    public void runMonthlyJob() {
        try {
            System.out.println("⏰ Lancement du batch programmé (CRON 1er du mois à 1h00)...");
            
            JobParameters params = new JobParametersBuilder()
                    .addString("JobID", String.valueOf(System.currentTimeMillis()))
                    .addDate("timestamp", new Date())
                    .toJobParameters();
            
            jobLauncher.run(payrollJob, params);
            
            System.out.println("✅ Job 'payrollJob' (CRON) lancé avec succès.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}