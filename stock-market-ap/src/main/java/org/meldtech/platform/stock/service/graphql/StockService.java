package org.meldtech.platform.stock.service.graphql;

import org.meldtech.platform.stock.dto.StockRequest;
import org.meldtech.platform.stock.model.Stock;
import org.meldtech.platform.stock.repository.StockRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@Service
public class StockService {
    private final StockRepository repository;
    private final Sinks.Many<Stock> stockSink = Sinks.many().multicast().onBackpressureBuffer();

    public StockService(StockRepository repository) {
        this.repository = repository;
    }

    // Declare methods here to expose GraphQL API
    public Mono<Stock> getStock(String symbol) {
        return repository.findBySymbol(symbol);
    }

    public Mono<Stock> saveStock(StockRequest stock) {
        return repository.save(Stock.builder()
                        .symbol(stock.symbol())
                        .name(stock.name())
                        .price(stock.price())
                .build());
    }

    public Flux<Stock> stockPriceUpdates(String symbol) {
        return stockSink.asFlux().filter(stock -> stock.symbol().equals(symbol));
    }

    // Simulate stock price update event
    @Scheduled(fixedRate = 5000)
    public void updateStockPrices() {
        System.out.println("Updating stock prices...");
        repository.findAll()
                .map(stock ->  Stock.builder()
                             .id(stock.id())
                            .symbol(stock.symbol())
                            .name(stock.name())
                            .price(stock.price() + Math.random() - 0.5)
                            .build()
                )
                .doOnNext(stockSink::tryEmitNext)
                .subscribe(repository::save);
    }
}
