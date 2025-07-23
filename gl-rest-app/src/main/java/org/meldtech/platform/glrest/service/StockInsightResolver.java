package org.meldtech.platform.glrest.service;

import org.meldtech.platform.glrest.dto.StockInsightRequest;
import org.meldtech.platform.glrest.dto.StockInsightResponse;
import org.meldtech.platform.glrest.utl.GraphQlResolver;
import org.meldtech.platform.glrest.utl.GraphQlUtil;
import org.springframework.graphql.client.GraphQlClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

import static org.meldtech.platform.glrest.utl.GraphQlOps.QUERY;
import static org.meldtech.platform.glrest.utl.GraphQlOps.MUTATION;

@Service
public class StockInsightResolver {
    private final GraphQlClient graphQlClient;

    public StockInsightResolver(GraphQlClient graphQlClient) {
        this.graphQlClient = graphQlClient;
    }

    public Mono<StockInsightResponse> getStockInsightBridge(String symbol, String selectedFields) {
//        language=GraphQL
//        String query = """
//            query($symbol: String!, $userPublicId: String!) {
//              getStockInsight(symbol: $symbol, userPublicId: $userPublicId) {
//                symbol
//                marketCap
//                peRatio
//                revenueGrowth
//              }
//            }
//        """;
        return GraphQlResolver.builder(graphQlClient)
                .resolve(QUERY, "getStockInsight", selectedFields,
                        Map.of("symbol", "String!"), Map.of("symbol", symbol), StockInsightResponse.class);
}

    public Mono<List<StockInsightResponse>> getStockInsightsBridge(String selectedFields) {
        return GraphQlResolver.builder(graphQlClient)
                .resolveMany(QUERY, "getStockInsights", selectedFields, Map.of(), Map.of(), StockInsightResponse.class);
    }

    public Mono<StockInsightResponse> addStockInsight(StockInsightRequest request, String selectedFields) {
        return GraphQlResolver.builder(graphQlClient)
                .resolve(MUTATION, "addStockInsight", selectedFields,
                        Map.of("request", "StockInsightRequest!"), Map.of("request", request),
                        StockInsightResponse.class);
    }

    public Mono<StockInsightResponse> updateStockInsight(StockInsightRequest request, String selectedFields) {
        return GraphQlResolver.builder(graphQlClient)
                .resolve(MUTATION, "updateStockInsight", selectedFields,
                        Map.of("request", "StockInsightRequest!"), Map.of("request", request),
                        StockInsightResponse.class);
    }
}
