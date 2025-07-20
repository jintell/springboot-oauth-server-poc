package org.meldtech.platform.stock.repository;

import org.meldtech.platform.stock.model.Stock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.List;

public interface StockRepository extends ReactiveCrudRepository<Stock, Integer> {
    Mono<Stock> findBySymbol(String symbol);
    Flux<Stock> findBySymbolIn(Collection<String> symbols);
}
