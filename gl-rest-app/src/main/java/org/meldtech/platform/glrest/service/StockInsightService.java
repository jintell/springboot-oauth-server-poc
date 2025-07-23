package org.meldtech.platform.glrest.service;

import org.meldtech.platform.glrest.dto.StockInsightRequest;
import org.meldtech.platform.glrest.dto.StockInsightResponse;
import org.meldtech.platform.glrest.model.StockInsight;
import org.meldtech.platform.glrest.repository.StockInsightRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class StockInsightService {
    private final StockInsightRepository repository;

    public StockInsightService(StockInsightRepository repository) {
        this.repository = repository;
    }

    public Mono<StockInsightResponse> getStockInsight(String symbol) {
        return repository.findBySymbol(symbol.toUpperCase())
                .map(this::mapToResponse);
    }

    public Mono<List<StockInsightResponse>> getAllStockInsight() {
        return repository.findAll()
                .collectList()
                .map(this::mapToResponse);
    }

    public Mono<StockInsightResponse> saveStockInsight(StockInsightRequest request) {
        return repository.save(StockInsight.builder()
                .symbol(request.symbol().toUpperCase())
                .name(request.name())
                .marketCap(request.marketCap())
                .peRatio(request.peRatio())
                .revenueGrowth(request.revenueGrowth())
                .price(request.price())
                .build()).map(this::mapToResponse);
    }

    public Mono<StockInsightResponse> updateStockInsight(StockInsightRequest request) {
        return repository.findBySymbol(request.symbol().toUpperCase())
                .flatMap(insight -> repository.save(StockInsight.builder()
                        .id(insight.id())
                        .symbol(request.symbol().toUpperCase())
                        .name(request.name())
                        .marketCap(request.marketCap())
                        .peRatio(request.peRatio())
                        .revenueGrowth(request.revenueGrowth())
                        .price(request.price())
                        .build()
                )).map(this::mapToResponse);
    }

    private StockInsightResponse mapToResponse(StockInsight insight) {
        return StockInsightResponse.builder()
                .symbol(insight.symbol())
                .name(insight.name())
                .marketCap(insight.marketCap())
                .peRatio(insight.peRatio())
                .revenueGrowth(insight.revenueGrowth())
                .price(insight.price())
                .build();
    }

    private List<StockInsightResponse> mapToResponse(List<StockInsight> insights) {
        return insights.stream()
                .map(this::mapToResponse)
                .toList();
    }

}
