package com.classified.seller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(exclude = KafkaAutoConfiguration.class)
@ComponentScan(basePackages = {"com.classified.seller.commons.*" , "com.classified.seller"})
@EntityScan(basePackages = {"com.classified.seller.commons.*" , "com.classified.seller"})
@EnableJpaRepositories({"com.classified.seller.commons.*"})
public class AnomalyDetectionApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(AnomalyDetectionApplication.class, args);
	}

}
