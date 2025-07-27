package com.jade.platform.appointments.dto;

import lombok.Builder;

@Builder
public record AppointmentResponse(String doctorName,
                                  String patientName,
                                  String location,
                                  String appointmentDate,
                                  String appointmentTime,
                                  String message) {
}
