package com.ms.gestionHistoireTicket.gestionHistoireTicketService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableFeignClients
public class GestionHistoireTicketServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GestionHistoireTicketServiceApplication.class, args);
	}
	@Configuration
	public class RestTemplateConfig {
		@Bean
		public RestTemplate restTemplate(RestTemplateBuilder builder) {
			return builder.build();
		}
	}
}
