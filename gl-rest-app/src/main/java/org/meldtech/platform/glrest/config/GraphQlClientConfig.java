package org.meldtech.platform.glrest.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.graphql.client.HttpGraphQlClient;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@EnableScheduling
@Configuration
public class GraphQlClientConfig {
    @Value("${graphql.endpoint}")
    private String endpoint;
    @Bean
    public GraphQlClient graphQlClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(endpoint) // Replace with your GraphQL API endpoint
                .build();
        return HttpGraphQlClient.builder(webClient).build();
    }
}
