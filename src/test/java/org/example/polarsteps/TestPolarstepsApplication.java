package org.example.polarsteps;

import org.example.polarsteps.support.TestcontainersConfiguration;
import org.springframework.boot.SpringApplication;

public class TestPolarstepsApplication {

	public static void main(String[] args) {
		SpringApplication.from(PolarstepsApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
