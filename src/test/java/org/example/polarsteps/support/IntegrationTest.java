package org.example.polarsteps.support;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import({ TestcontainersConfiguration.class, DatabaseCleaner.class })
public abstract class IntegrationTest {

	@Autowired
	protected MockMvcTester mvc;

	@Autowired
	protected ObjectMapper objectMapper;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@BeforeEach
	void cleanDatabase() {
		this.databaseCleaner.clean();
	}

}
