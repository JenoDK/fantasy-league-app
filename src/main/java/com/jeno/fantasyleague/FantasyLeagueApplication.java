package com.jeno.fantasyleague;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEmailTools
public class FantasyLeagueApplication {

	public static void main(String[] args) {
		SpringApplication.run(FantasyLeagueApplication.class, args);
	}
}
