package org.meldtech.platform.security;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.In;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public static final String API_KEY_HEADER_NAME = "X-API-KEY";

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Spring WebFlux API Key Example")
                        .version("1.0")
                        .description("Secured WebFlux app with API Key"))
                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
                .components(new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("ApiKeyAuth",
                                new SecurityScheme()
                                        .name(API_KEY_HEADER_NAME)
                                        .type(Type.APIKEY)
                                        .in(In.HEADER)
                        )
                );
    }
}
