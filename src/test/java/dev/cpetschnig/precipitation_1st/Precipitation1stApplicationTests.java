package dev.cpetschnig.precipitation_1st;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class Precipitation1stApplicationTests {

	@Test
	void contextLoads() {
	}

}
