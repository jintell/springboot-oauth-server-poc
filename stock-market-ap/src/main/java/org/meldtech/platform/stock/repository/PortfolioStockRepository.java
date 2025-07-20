package org.meldtech.platform.stock.repository;

import org.meldtech.platform.stock.model.PortfolioStock;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface PortfolioStockRepository extends ReactiveCrudRepository<PortfolioStock, Integer> {
    Flux<PortfolioStock> findByPortfolioId(Integer portfolioId);
}
