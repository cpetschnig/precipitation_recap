package dev.cpetschnig.precipitation_recap;

import dev.cpetschnig.precipitation_recap.formatter.CliOutputFormatter;
import dev.cpetschnig.precipitation_recap.open_meteo.Archive;
import dev.cpetschnig.precipitation_recap.open_meteo.Client;
import dev.cpetschnig.precipitation_recap.open_meteo.RequestParamsBuilder;
import net.minidev.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.http.HttpClient;
import java.time.LocalDate;
import java.util.Optional;

@SpringBootApplication
public class Application implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) {

		Logger logger = LoggerFactory.getLogger(Application.class);
		logger.info("Entering run...");

		Client client = new Client(HttpClient.newHttpClient());

		LocalDate startDate = LocalDate.now().minusWeeks(1).minusDays(2);
		LocalDate endDate = startDate.plusWeeks(1);
		RequestParamsBuilder requestParamsBuilder = RequestParamsBuilder.create(46.68, 14.35, startDate, endDate);
		Optional<JSONObject> json = client.call(requestParamsBuilder);

		if (json.isPresent()) {
			logger.info("Successfully executed client call");

			Archive archive = new Archive(json.get());

			CliOutputFormatter formatter = new CliOutputFormatter(archive, startDate, endDate);
			formatter.print();
		} else {
			logger.error("Failed to executed client call");
		}
	}
}
