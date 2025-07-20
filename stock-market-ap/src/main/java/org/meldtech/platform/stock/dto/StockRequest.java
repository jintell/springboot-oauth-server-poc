package org.meldtech.platform.stock.dto;

import jakarta.annotation.Nonnull;

import javax.annotation.Nonnegative;

public record StockRequest(@Nonnull
                           String symbol,
                           @Nonnull String name,
                           @Nonnegative
                           double price) {
}
