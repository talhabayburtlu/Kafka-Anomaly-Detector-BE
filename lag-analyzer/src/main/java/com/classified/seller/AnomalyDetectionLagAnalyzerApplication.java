package com.classified.seller;

import com.classified.seller.commons.service.LagService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableJpaRepositories
public class AnomalyDetectionLagAnalyzerApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(AnomalyDetectionLagAnalyzerApplication.class, args);
		LagService lagAnalyzerService = applicationContext.getBean(LagService.class);
		System.out.println("[AnomalyDetectionLagAnalyzerApplication#Timer] Starting to analyze lags.");
		lagAnalyzerService.analyzeLag();
		System.out.println("[AnomalyDetectionLagAnalyzerApplication#Timer] Finished analyzing lags.");
		applicationContext.close();
	}
}
