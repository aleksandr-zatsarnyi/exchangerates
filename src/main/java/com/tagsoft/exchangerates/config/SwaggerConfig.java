package com.tagsoft.exchangerates.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Exchange Rate API").version("1.0"))
                .components(new Components())
                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
                .addServersItem(new Server().url("http://localhost:8080"));
    }
}
