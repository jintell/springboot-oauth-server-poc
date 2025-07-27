package com.jade.platform.doctors.dto;

import jakarta.annotation.Nonnull;

public record DoctorRecordRequest(@Nonnull String firstName,
                                  @Nonnull String lastName,
                                  @Nonnull String email,
                                  @Nonnull String phone,
                                  String location,
                                  @Nonnull String specialty,
                                  String centerName) {
}
