package org.meldtech.platform.stock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class StockMarketApApplication {

	public static void main(String[] args) {
		SpringApplication.run(StockMarketApApplication.class, args);
	}

}
