package org.meldtech.platform.stock.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.io.Serializable;
import java.time.Instant;

@Builder
@Table("public.portfolios")
public record Portfolio(@Id
                        Integer id,
                        String userId,
                        Instant createdOn) implements Serializable, Persistable<Integer> {
    public Portfolio {
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
