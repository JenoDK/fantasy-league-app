package com.jeno.fantasyleague;

import java.util.TimeZone;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.jeno.fantasyleague.backend.data.service.email.ApplicationEmailService;

@SpringBootApplication
public class FantasyLeagueApplication {

	private static final Logger LOG = LogManager.getLogger(FantasyLeagueApplication.class.getName());

	public static void main(String[] args) {
		SpringApplication.run(FantasyLeagueApplication.class, args);
		TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
		LOG.info(String.format("Running with heap size: %s, heap max size: %s, heap free size: %s", Runtime.getRuntime().totalMemory(), Runtime.getRuntime().maxMemory(), Runtime.getRuntime().freeMemory()));
	}
}
