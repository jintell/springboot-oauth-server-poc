package com.jade.platform.patients.dto;

import jakarta.annotation.Nonnull;

public record PatientRecordRequest(@Nonnull String email,
                                   @Nonnull String firstName,
                                   @Nonnull String lastName,
                                   @Nonnull String phone,
                                   @Nonnull String address) {
}
