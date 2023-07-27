package com.pi.stepup.global.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        Info info = new Info()
            .version("v1")
            .title("Step Up API")
            .description("Step Up! _ PI team");

        return new OpenAPI().info(info);
    }
}
