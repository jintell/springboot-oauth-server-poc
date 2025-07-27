package com.jade.platform.appointments.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalTime;

@Builder
@Table("public.appointments")
public record Appointment(@Id Integer id,
                          String patientId,
                          String doctorId,
                          String patientName,
                          String doctorName,
                          String location,
                          LocalDate appointmentDate,
                          LocalTime appointmentTime,
                          String reason) implements Persistable<Integer> {
    @Override
    public Integer getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return id == null;
    }
}
