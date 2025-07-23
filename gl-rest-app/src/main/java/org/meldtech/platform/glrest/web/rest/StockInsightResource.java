package org.meldtech.platform.glrest.web.rest;

import lombok.RequiredArgsConstructor;
import org.meldtech.platform.glrest.dto.StockInsightRequest;
import org.meldtech.platform.glrest.dto.StockInsightResponse;
import org.meldtech.platform.glrest.event.subscription.StockInsightSubscription;
import org.meldtech.platform.glrest.service.StockInsightResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/v1/stocks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000/")
public class StockInsightResource {
    private final StockInsightResolver service;
    private final StockInsightSubscription subscription;

    @GetMapping("/{symbol}/insight/metrics")
    public Mono<ResponseEntity<StockInsightResponse>> getStockInsightViaRest(
            @PathVariable String symbol,
            @RequestParam String q) {
        return service.getStockInsightBridge(symbol, q)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/insight/metrics")
    public Mono<ResponseEntity<List<StockInsightResponse>>> getStockInsightsViaRest(
            @RequestParam String q) {
        return service.getStockInsightsBridge(q)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/insight/metrics")
    public Mono<ResponseEntity<StockInsightResponse>> addStockInsightViaRest(
            @RequestBody StockInsightRequest request,
            @RequestParam String q) {
        return service.addStockInsight(request, q)
                .map(ResponseEntity::ok);
    }

    @PatchMapping("/insight/metrics")
    public Mono<ResponseEntity<StockInsightResponse>> updateStockInsightViaRest(
            @RequestBody StockInsightRequest request,
            @RequestParam String q) {
        return service.updateStockInsight(request, q)
                .flatMap(subscription::updateStockInsight)
                .doOnNext(stockInsightResponse -> System.out.println("Updated: " + stockInsightResponse))
                .map(ResponseEntity::ok);
    }
}
