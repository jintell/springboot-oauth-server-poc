package com.jade.platform.patients.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("public.patients")
public record Patient(@Id Integer id,
                      String firstName,
                      String lastName,
                      String email, String phone,
                      String address) implements Persistable<Integer> {
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
