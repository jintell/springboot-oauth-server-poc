package org.meldtech.platform.glrest.utl;

import lombok.NonNull;
import org.springframework.graphql.client.GraphQlClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

public class GraphQlUtil {

    private final GraphQlOps header;
    private final String method;
    private final Map<String, Object> params;
    private final String fields;
    private final GraphQlClient graphQlClient;

    private String document;

    private GraphQlUtil(GraphQlBuilder builder) {
        header = builder.header;
        method = builder.method;
        params = builder.params;
        fields = builder.fields;
        graphQlClient = builder.graphQlClient;
    }

    public static GraphQlBuilder builder() {
        return new GraphQlBuilder();
    }

    public static class GraphQlBuilder {
        private GraphQlOps header;
        private String method;
        private Map<String, Object> params;
        private String fields;
        private GraphQlClient graphQlClient;

        public GraphQlBuilder() {
            header = GraphQlOps.QUERY;
            method = "";
            params = Map.of();
            fields = "";
            graphQlClient = null;
        }

        public GraphQlBuilder header(@NonNull GraphQlOps header) {
            this.header = header;
            return this;
        }

        public GraphQlBuilder method(@NonNull String method) {
            this.method = method;
            return this;
        }

        public GraphQlBuilder params(Map<String, Object> params) {
            this.params = params;
            return this;
        }

        public GraphQlBuilder fields(String fields) {
            this.fields = fields;
            return this;
        }

        public GraphQlBuilder client(GraphQlClient graphQlClient) {
            this.graphQlClient = graphQlClient;
            return this;
        }

        public GraphQlUtil build() {
            if (method.isEmpty()) throw new IllegalArgumentException("Method cannot be empty");
            if (fields.isEmpty()) throw new IllegalArgumentException("Fields cannot be empty");
            if (graphQlClient == null) throw new IllegalArgumentException("GraphQlClient cannot be null");
            return new GraphQlUtil(this);
        }
    }

    public GraphQlUtil buildQuery() {
        StringBuilder queryBuilder = new StringBuilder(header.getValue());

        if (!params.isEmpty()) {
            queryBuilder.append("(");
            params.forEach((key, value) -> queryBuilder.append("$")
                    .append(key).append(": ").append(value).append(", "));
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(") {");
        }else queryBuilder.append(" ").append(" {");

        if (!params.isEmpty()) {
            queryBuilder.append(" ").append(method).append("(");
            params.forEach((key, value) -> queryBuilder
                    .append(key).append(": $").append(key).append(", "));
            queryBuilder.delete(queryBuilder.length() - 2, queryBuilder.length());
            queryBuilder.append(") { ");
        }else queryBuilder.append(" ").append(method).append(" { ");

        getFields().forEach(field -> queryBuilder.append(field).append(" "));
        queryBuilder.append("} }");

        document = queryBuilder.toString();

        return this;
    }

    public  <T> Mono<T> retrieve(Map<String, Object> variables, String method, Class<T> clazz) {
        return graphQlClient.document(document)
                .variables(variables)
                .retrieve(method)
                .toEntity(clazz);
    }

    public <T> Mono<List<T>> retrieveMany(Map<String, Object> variables, String method, Class<T> clazz) {
        return graphQlClient.document(document)
                .variables(variables)
                .retrieve(method)
                .toEntityList(clazz);
    }

    private List<String> getFields() {
        return List.of(fields.split(","));
    }

}
