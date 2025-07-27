package com.jade.platform.patients.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public record PatientRecordResponse(Integer patientId,
                                    String email,
                                    String firstName,
                                    String lastName,
                                    String phone,
                                    String address) {
}
