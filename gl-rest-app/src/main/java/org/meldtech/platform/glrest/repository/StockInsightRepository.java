package org.meldtech.platform.glrest.repository;

import org.meldtech.platform.glrest.model.StockInsight;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface StockInsightRepository extends ReactiveCrudRepository<StockInsight, Integer> {
    Mono<StockInsight> findBySymbol(String symbol);
}
