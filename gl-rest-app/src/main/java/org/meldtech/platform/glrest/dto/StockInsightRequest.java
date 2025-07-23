package org.meldtech.platform.glrest.dto;

import jakarta.annotation.Nonnull;

public record StockInsightRequest(@Nonnull String symbol,
                                  @Nonnull String name,
                                  double marketCap,
                                  double peRatio,
                                  double revenueGrowth,
                                  double price) {
}
