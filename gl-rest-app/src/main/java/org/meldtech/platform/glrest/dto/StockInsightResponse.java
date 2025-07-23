package org.meldtech.platform.glrest.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record StockInsightResponse(String symbol,
                                   String name,
                                   Double marketCap,
                                   Double peRatio,
                                   Double revenueGrowth,
                                   Double price) {
}
