//package org.meldtech.platform.stock.service.graphql;
//
//import org.meldtech.platform.stock.model.Stock;
//import org.springframework.graphql.data.method.annotation.Argument;
//import org.springframework.graphql.data.method.annotation.SubscriptionMapping;
//import org.springframework.stereotype.Component;
//import reactor.core.publisher.Flux;
//
//@Component
//public class StockSubscription {
//    private final Flux<Stock> stockPriceFlux;
//
//    public StockSubscription(StockPricePublisher publisher) {
//        this.stockPriceFlux = publisher.getStockPriceFlux();
//    }
//
//    @SubscriptionMapping
//    public Flux<Stock> stockPriceUpdates(@Argument String symbol) {
//        return stockPriceFlux.filter(stock -> stock.symbol().equals(symbol));
//    }
//}
