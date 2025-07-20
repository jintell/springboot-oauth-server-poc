package org.meldtech.platform.stock.resource;

import org.meldtech.platform.stock.dto.StockRequest;
import org.meldtech.platform.stock.model.Stock;
import org.meldtech.platform.stock.service.graphql.StockService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class StockResource {
    private final StockService stockService;

    public StockResource(StockService stockService) {
        this.stockService = stockService;
    }

    @QueryMapping
    public Mono<Stock> getStock(@Argument String symbol) {
        return stockService.getStock(symbol);
    }

    @MutationMapping
    public Mono<Stock> addStock(@Argument StockRequest request) {
        return stockService.saveStock(request);
    }

    @SubscriptionMapping
    public Flux<Stock> stockPriceUpdates(@Argument String symbol) {
        return stockService.stockPriceUpdates(symbol);
    }
}
