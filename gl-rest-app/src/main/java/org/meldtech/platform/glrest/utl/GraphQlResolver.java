package org.meldtech.platform.glrest.utl;

import org.springframework.graphql.client.GraphQlClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class GraphQlResolver {
    private final GraphQlClient client;

    private GraphQlResolver(GraphQlClient client) {
        this.client = client;
    }

    public static GraphQlResolver builder(GraphQlClient client) {
        return new GraphQlResolver(client);
    }

    public <T> Mono<T> resolve(GraphQlOps type,
                               String method,
                               String selectedFields,
                               Map<String, Object> params,
                               Map<String, Object> variables,
                               Class<T> clazz) {
        return GraphQlUtil.builder()
                .client(client)
                .header(type)
                .method(method)
                .params(params)
                .fields(selectedFields)
                .build()
                .buildQuery()
                .retrieve(variables, method, clazz);
    }

    public <T> Mono<List<T>> resolveMany(GraphQlOps type,
                                         String method,
                                         String selectedFields,
                                         Map<String, Object> params,
                                         Map<String, Object> variables,
                                         Class<T> clazz) {
        return GraphQlUtil.builder()
                .client(client)
                .header(type)
                .method(method)
                .params(params)
                .fields(selectedFields)
                .build()
                .buildQuery()
                .retrieveMany(variables, method, clazz);
    }
}
