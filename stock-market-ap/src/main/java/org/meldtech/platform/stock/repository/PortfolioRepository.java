package org.meldtech.platform.stock.repository;

import org.meldtech.platform.stock.model.Portfolio;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PortfolioRepository extends ReactiveCrudRepository<Portfolio, Integer> {
    Mono<Portfolio> findByUserId(String userId);
}
