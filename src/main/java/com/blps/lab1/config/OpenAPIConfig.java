package com.blps.lab1.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BLPS Lab1 API")
                        .description("API for Business Logic of Program Systems Laboratory Work 1")
                        .version("1.0")
                        .contact(new Contact()
                                .name("BLPS Team")
                                .email("example@example.com"))
                        .license(new License()
                                .name("MIT License")));
    }
} 