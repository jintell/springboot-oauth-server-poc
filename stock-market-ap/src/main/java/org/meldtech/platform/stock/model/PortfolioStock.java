package org.meldtech.platform.stock.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Table("public.portfolio_stocks")
public record PortfolioStock(@Id
                             Integer id,
                             Integer portfolioId,
                             String stockId,
                             Instant createdOn) implements Serializable, Persistable<Integer> {
    public PortfolioStock {
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
