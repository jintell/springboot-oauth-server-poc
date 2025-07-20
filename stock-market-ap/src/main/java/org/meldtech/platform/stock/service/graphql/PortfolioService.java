package org.meldtech.platform.stock.service.graphql;

import org.meldtech.platform.stock.dto.PortfolioRequest;
import org.meldtech.platform.stock.dto.PortfolioResponse;
import org.meldtech.platform.stock.model.Portfolio;
import org.meldtech.platform.stock.model.PortfolioStock;
import org.meldtech.platform.stock.model.Stock;
import org.meldtech.platform.stock.repository.PortfolioRepository;
import org.meldtech.platform.stock.repository.PortfolioStockRepository;
import org.meldtech.platform.stock.repository.StockRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class PortfolioService {
    private final PortfolioRepository repository;
    private final PortfolioStockRepository pRepository;
    private final StockRepository stockRepository;

    public PortfolioService(PortfolioRepository repository,
                            PortfolioStockRepository pRepository,
                            StockRepository stockRepository) {
        this.repository = repository;
        this.pRepository = pRepository;
        this.stockRepository = stockRepository;
    }

    public Mono<PortfolioResponse> save(PortfolioRequest request) {
        return savePortfolio(Portfolio.builder()
                .userId(request.userId())
                .build())
                .flatMap(portfolio -> savePortfolioStock(request, portfolio));
    }

    public Mono<PortfolioResponse> portfolioByUserId(String userId){
        return repository.findByUserId(userId)
                .flatMap(portfolio -> getStocks(portfolio.id())
                        .map(stocks -> PortfolioResponse.builder()
                                .userId(portfolio.userId())
                                .createdOn(portfolio.createdOn())
                                .stocks(stocks)
                                .build())
                );
    }

    private Mono<Portfolio> savePortfolio(Portfolio portfolio){
        return repository.save(portfolio);
    }

    private Mono<PortfolioResponse> savePortfolioStock(PortfolioRequest request, Portfolio portfolio){
        System.out.println("Saving stocks to portfolio: "+request.stockSymbols());
        return Mono.justOrEmpty(request.stockSymbols())
                .flatMapMany(Flux::fromIterable)
                .flatMap(stockRepository::findBySymbol)
                .doOnNext(System.out::println)
                .flatMap(stock -> pRepository.save(PortfolioStock.builder()
                                .stockId(stock.symbol())
                                .portfolioId(portfolio.id())
                        .build()))
                .doOnNext(System.out::println)
                .then(Mono.just(PortfolioResponse.builder()
                                .userId(portfolio.userId())
                                .createdOn(portfolio.createdOn())
                        .build()));
    }

    private Mono<List<Stock>> getStocks(Integer portfolioId){
        return pRepository.findByPortfolioId(portfolioId)
                .map(PortfolioStock::stockId)
                .collectList()
                .flatMapMany(stockRepository::findBySymbolIn)
                .collectList();
    }
}
