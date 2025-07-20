package org.meldtech.platform.stock.service.grpc;

import io.grpc.stub.StreamObserver;
import org.meldtech.platform.stock.repository.StockRepository;
import org.springframework.grpc.server.service.GrpcService;
import reactor.core.publisher.Flux;

import java.time.Duration;

@GrpcService
public class StockGrpcService extends StockMarketServiceGrpc.StockMarketServiceImplBase {
    private final StockRepository repository;

    public StockGrpcService(StockRepository repository) {
        this.repository = repository;
    }

    @Override
    public void getStockPrice(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        repository.findBySymbol(request.getSymbol())
                .map(stock -> StockResponse.newBuilder()
                        .setId(stock.id())
                        .setSymbol(stock.symbol())
                        .setName(stock.name())
                        .setPrice((float) stock.price())
                        .build())
                .doOnNext(responseObserver::onNext)
                .doFinally(signal -> responseObserver.onCompleted())
                .subscribe();
    }

    @Override
    public void streamStockPrices(StockRequest request, StreamObserver<StockResponse> responseObserver) {
        Flux.interval(Duration.ofSeconds(1))
                .flatMap(tick -> repository.findBySymbol(request.getSymbol()))
                .map(stock -> StockResponse.newBuilder()
                        .setId(stock.id())
                        .setName(stock.name())
                        .setSymbol(stock.symbol())
                        .setPrice((float) stock.price())
                        .build())
                .doOnNext(responseObserver::onNext)
                .subscribe();
    }
}
