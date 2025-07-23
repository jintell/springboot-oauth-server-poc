package org.meldtech.platform.glrest.web.graphql;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.glrest.dto.StockInsightRequest;
import org.meldtech.platform.glrest.dto.StockInsightResponse;
import org.meldtech.platform.glrest.event.subscription.StockInsightSubscription;
import org.meldtech.platform.glrest.service.StockInsightService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class StockInsightGqlController {
    private final StockInsightService service;
    private final StockInsightSubscription subscription;

    @QueryMapping
    public Mono<StockInsightResponse> getStockInsight(@Argument String symbol) {
        return service.getStockInsight(symbol);
    }

    @QueryMapping
    public Mono<List<StockInsightResponse>> getStockInsights() {
        return service.getAllStockInsight();
    }

    @QueryMapping
    public Mono<List<StockInsightResponse>> getStockInsightDetails() {
        return service.getAllStockInsight();
    }

    @MutationMapping
    public Mono<StockInsightResponse> addStockInsight(@Argument StockInsightRequest request) {
        return service.saveStockInsight(request);
    }

    @MutationMapping
    public Mono<StockInsightResponse> updateStockInsight(@Argument StockInsightRequest request) {
        return service.updateStockInsight(request);
    }

    @SubscriptionMapping
    public Flux<StockInsightResponse> updateStockInsightPrice(@Argument String symbol) {
        return subscription.stockPriceUpdate(symbol);
    }
}
