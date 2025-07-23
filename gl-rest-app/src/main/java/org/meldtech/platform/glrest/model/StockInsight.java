package org.meldtech.platform.glrest.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;

@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Builder
@Table("public.stock_insights")
public record StockInsight(@Id Integer id,
                           String symbol,
                           String name,
                           Double marketCap,
                           Double peRatio,
                           Double revenueGrowth,
                           Double price,
                           Instant createdOn) implements Serializable, Persistable<Integer> {
    public StockInsight {
        if(isNew()) { createdOn = Instant.now(); }
    }

    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
