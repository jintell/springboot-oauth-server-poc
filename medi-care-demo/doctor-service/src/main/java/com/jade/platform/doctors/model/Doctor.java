package com.jade.platform.doctors.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

@Builder
@Table("public.doctors")
public record Doctor(@Id
                     Integer id,
                     String firstName,
                     String lastName,
                     String email,
                     String phone,
                     String location,
                     String specialty,
                     String centerName) implements Persistable<Integer> {
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
