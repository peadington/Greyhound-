package com.greyhound;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.greyhound.property.FileStorageProperties;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties({ FileStorageProperties.class })
public class GreyhoundApplication {

	public static void main(String[] args) {
		SpringApplication.run(GreyhoundApplication.class, args);

//		LocalDate startDate = LocalDate.of(2021, 01, 01);
//		LocalDate endDate = LocalDate.of(2021, 02, 01);

		// GreyhoundRatingScraperService rating = new GreyhoundRatingScraperService();
		// rating.startProcess();

		//LocalDate startDate = LocalDate.now();
		//GreyhoundScraperServiceImpl greyhoundService = new GreyhoundScraperServiceImpl();
		//greyhoundService.startScrapper(startDate);
	}

}
