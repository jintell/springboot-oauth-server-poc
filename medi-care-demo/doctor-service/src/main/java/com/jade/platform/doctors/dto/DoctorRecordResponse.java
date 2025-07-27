package com.jade.platform.doctors.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record DoctorRecordResponse(Integer doctorId,
                                   String firstName,
                                   String lastName,
                                   String email,
                                   String phone,
                                   String location,
                                   String specialty,
                                   String centerName) {
}
