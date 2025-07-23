package org.meldtech.platform.glrest.event.subscription;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.glrest.dto.StockInsightResponse;
import org.meldtech.platform.glrest.service.StockInsightService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
@RequiredArgsConstructor
public class StockInsightSubscription {
    private final StockInsightService service;
    private final Sinks.Many<StockInsightResponse> stockSink = Sinks.many().multicast().onBackpressureBuffer();

    public Flux<StockInsightResponse> stockPriceUpdate(String symbol) {
        return stockSink.asFlux().filter(stock -> stock.symbol().equalsIgnoreCase(symbol));
    }

    public Mono<StockInsightResponse> updateStockInsight(StockInsightResponse response) {
        return Mono.just(response)
                .map(stockSink::tryEmitNext)
                .map(Sinks.EmitResult::isSuccess)
                .thenReturn(response);
    }

    // Simulate stock price update event
    @Scheduled(fixedRate = 5000)
    public void updateStockPrices() {
        System.out.println("Updating stock prices");
        service.getAllStockInsight()
                .flatMapMany(Flux::fromIterable)
                .map(stock ->  StockInsightResponse.builder()
                        .symbol(stock.symbol())
                        .name(stock.name())
                        .marketCap(stock.marketCap())
                        .peRatio(stock.peRatio())
                        .revenueGrowth(stock.revenueGrowth())
                        .price(stock.price() + Math.random() - 2.5)
                        .build()
                )
                .doOnNext(stockSink::tryEmitNext)
                .subscribe();
    }
}
