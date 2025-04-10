package com.usercard.manager.configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI(
            @Value("${apiTitle}") String apiTitle,
            @Value("${apiDescription}") String apiDescription,
            @Value("${apiVersion}") String apiVersion,
            @Value("${apiContactName}") String apiContactName,
            @Value("${apiContactEmail}") String apiContactEmail

    ) {
        return new OpenAPI()
                .components(new Components())
                .addServersItem(new Server()
                        .url("http://localhost:8888")
                        .description("Local development server"))
                .info(new Info()
                        .title(apiTitle)
                        .description(apiDescription)
                        .version(apiVersion)
                        .contact(new Contact().name(apiContactName).email(apiContactEmail))
                );
    }
}
