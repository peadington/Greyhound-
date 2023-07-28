package com.greyhound.schedular;

import java.time.LocalDate;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class GreyhoundScheduler {

	@Scheduled(cron = "0 0 1 * * *")
	//@EventListener(ApplicationReadyEvent.class)
	public void startScrapper() {
		LocalDate startDate = LocalDate.now().minusDays(1);
		new Thread(new RunnableScrapper(startDate)).start();
	}
}
