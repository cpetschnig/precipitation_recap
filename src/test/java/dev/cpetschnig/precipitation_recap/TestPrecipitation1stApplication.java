package dev.cpetschnig.precipitation_recap;

import org.springframework.boot.SpringApplication;

public class TestPrecipitation1stApplication {

	public static void main(String[] args) {
		SpringApplication.from(Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
