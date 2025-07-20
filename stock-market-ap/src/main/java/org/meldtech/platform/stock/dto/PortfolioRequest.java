package org.meldtech.platform.stock.dto;

import jakarta.annotation.Nonnull;

import java.util.Arrays;
import java.util.List;

public record PortfolioRequest(@Nonnull String userId,
                               @Nonnull String stocks,
                               List<String> stockSymbols) {
    public PortfolioRequest {
         stockSymbols = Arrays.stream(stocks.replaceAll(" ", "").split(",")).toList();
    }
}
