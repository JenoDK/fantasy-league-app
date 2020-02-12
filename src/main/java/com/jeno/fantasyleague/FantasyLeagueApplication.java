package com.jeno.fantasyleague;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import it.ozimov.springboot.mail.configuration.EnableEmailTools;

@SpringBootApplication
@EnableEmailTools
public class FantasyLeagueApplication {

	public static void main(String[] args) {
		SpringApplication.run(FantasyLeagueApplication.class, args);
	}
}
