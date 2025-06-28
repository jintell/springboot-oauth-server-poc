package org.meldtech.platform.security;

import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {
    @Value("${springdoc.server-url}")
    private String serverUrl;

    public static final String API_KEY_HEADER_NAME = "X-API-KEY";

    @Bean
    public OpenAPI openAPI() {
        Server server = new Server();
        server.setUrl(serverUrl);
        return new OpenAPI()
                .servers(List.of(server))
                .info(new io.swagger.v3.oas.models.info.Info()
                        .title("Spring WebFlux API Key Example")
                        .version("1.0")
                        .description("Secured WebFlux app with API Key"))
//                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
                .components(
                        new io.swagger.v3.oas.models.Components()
                        .addSecuritySchemes("ApiKeyAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name(API_KEY_HEADER_NAME)
                        ).addSecuritySchemes("AdminApiKeyAuth",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.APIKEY)
                                        .in(SecurityScheme.In.HEADER)
                                        .name("X-ADMIN-API-KEY")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("ApiKeyAuth"))
                .addSecurityItem(new SecurityRequirement().addList("AdminApiKeyAuth"));
    }

}
