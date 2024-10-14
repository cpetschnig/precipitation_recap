package dev.cpetschnig.precipitation_1st;

import dev.cpetschnig.precipitation_1st.open_meteo.Archive;
import dev.cpetschnig.precipitation_1st.open_meteo.Client;
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
	public void run(String... args) throws Exception {

		Logger logger = LoggerFactory.getLogger(Application.class);
		logger.info("Entering run...");

		Client client = new Client(HttpClient.newHttpClient());
		Optional<JSONObject> json = client.call();

		if (json.isPresent()) {
			logger.info("Successfully executed client call");

			Archive archive = new Archive(json.get());
			double[] valuesForDay = archive.getPrecipitationForDay(LocalDate.now());
			for (int i = 0; i < valuesForDay.length; i++) {
				System.out.printf("%2d: %.2f%n", i, valuesForDay[i]);
			}
		} else {
			logger.error("Failed to executed client call");
		}
	}

}
