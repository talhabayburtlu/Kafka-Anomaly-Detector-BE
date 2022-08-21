package com.classified.seller;

import com.classified.seller.commons.service.AlertService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Timer;
import java.util.TimerTask;

@SpringBootApplication
@EnableJpaRepositories
public class AnomalyDetectionAlertProcessApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext applicationContext = SpringApplication.run(AnomalyDetectionAlertProcessApplication.class, args);
		AlertService alertService = applicationContext.getBean(AlertService.class);
		System.out.println("[AnomalyDetectionAlertProcessApplication#Timer] Starting to process alerts.");
		alertService.processAlerts();
		System.out.println("[AnomalyDetectionAlertProcessApplication#Timer] Finished processing alerts.");
		applicationContext.close();
	}

}
