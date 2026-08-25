package org.example.polarsteps;

import org.example.polarsteps.support.IntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.health.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.health.contributor.Status;

import static org.assertj.core.api.Assertions.assertThat;

class PolarstepsApplicationTests extends IntegrationTest {

	@Autowired
	private HealthEndpoint healthEndpoint;

	@Test
	void contextLoadsAndDatabaseIsHealthy() {
		assertThat(this.healthEndpoint.health().getStatus()).isEqualTo(Status.UP);
	}

}
