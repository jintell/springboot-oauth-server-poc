package com.jade.platform.appointments.dto;

import jakarta.annotation.Nonnull;

import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentRequest(@Nonnull String doctorEmail,
                                 @Nonnull String patientEmail,
                                 @Nonnull LocalDate appointmentDate,
                                 @Nonnull LocalTime appointmentTime,
                                 String reason) {
}
