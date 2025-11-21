package com.ksvtech.drivingschool.config;

import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI drivingSchoolOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Driving School Management API")
                        .version("1.0.0")
                        .description("APIs for student registration, batches, certificates with QR, etc."));
    }
}
