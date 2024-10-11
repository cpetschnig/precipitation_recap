package dev.cpetschnig.precipitation_1st;

import dev.cpetschnig.precipitation_1st.open_meteo.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.http.HttpClient;

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

		if (client.call()) {
			logger.info("Successfully executed client call");
		} else {
			logger.error("Failed to executed client call");
		}
	}

}
