package org.meldtech.platform.stock.resource;

import org.meldtech.platform.stock.dto.PortfolioRequest;
import org.meldtech.platform.stock.dto.PortfolioResponse;
import org.meldtech.platform.stock.service.graphql.PortfolioService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Mono;

@Controller
public class PortfolioResource {
    private final PortfolioService portfolioService;

    public PortfolioResource(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @QueryMapping
    public Mono<PortfolioResponse> getPortfolio(@Argument String userId) {
        return portfolioService.portfolioByUserId(userId);
    }

    @MutationMapping
    public Mono<PortfolioResponse> addPortfolio(@Argument PortfolioRequest request) {
        return portfolioService.save(request);
    }

}
