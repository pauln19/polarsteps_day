package org.example.polarsteps.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class OpenApiConfig {

    @Bean
    OpenAPI polarstepsOpenApi() {
        return new OpenAPI().info(new Info().title("Polarsteps API")
                .version("v1")
                .description("Errors are returned as RFC 9457 application/problem+json."));
    }

}
