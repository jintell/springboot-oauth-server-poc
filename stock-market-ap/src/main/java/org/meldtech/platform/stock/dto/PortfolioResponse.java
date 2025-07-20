package org.meldtech.platform.stock.dto;

import lombok.Builder;
import org.meldtech.platform.stock.model.Stock;

import java.time.Instant;
import java.util.List;

@Builder
public record PortfolioResponse(String userId, List<Stock> stocks, Instant createdOn) {
}
