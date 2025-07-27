package com.jade.platform.appointments.service;

import com.jade.platform.appointments.constants.AppointmentViewType;
import com.jade.platform.appointments.dto.AppointmentRequest;
import com.jade.platform.appointments.dto.AppointmentResponse;
import com.jade.platform.appointments.model.Appointment;
import com.jade.platform.appointments.repository.AppointmentRepository;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.jade.platform.proto.definition.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final DoctorServiceGrpc.DoctorServiceBlockingStub doctorService;
    private final PatientServiceGrpc.PatientServiceBlockingStub patientService;

    public Mono<AppointmentResponse> bookAppointment(AppointmentRequest request) {
        try {
            var doctorResponse = doctorService.getDoctorDetailWithEmail(DoctorDetailsEmailRequest.
                    newBuilder()
                    .setEmail(request.doctorEmail())
                    .build());
            var patientResponse = patientService.getPatientDetailByEmail(PatientRequestWithEmail
                    .newBuilder()
                    .setEmail(request.patientEmail())
                    .build());
            return appointmentRepository.save(toModel(request, doctorResponse, patientResponse))
                    .map(this::toResponse);
        }catch (StatusRuntimeException e){
            return Mono.error(e);
        }catch (Exception e){
            System.err.println(e.getMessage());
            return Mono.empty();
        }
    }

    public Flux<AppointmentResponse> viewAppointments(String personId, AppointmentViewType viewType) {
        return viewType == AppointmentViewType.DOCTOR ? viewAppointments(appointmentRepository.findByDoctorId(personId))
                : viewAppointments(appointmentRepository.findByPatientId(personId));
    }

    public Flux<AppointmentResponse> viewAppointments(LocalDate date) {
        return viewAppointments(appointmentRepository.findByAppointmentDate(date));
    }

    private Flux<AppointmentResponse> viewAppointments(Flux<Appointment> appointments) {
        return appointments.map(this::toResponse);
    }

    private Appointment toModel(AppointmentRequest request,
                                DoctorResponse doctorResponse,
                                PatientResponse patientResponse) {
        return Appointment.builder()
                .doctorId(doctorResponse.getEmail())
                .patientId(patientResponse.getEmail())
                .doctorName(doctorResponse.getFirstName() + " " + doctorResponse.getLastName())
                .patientName(patientResponse.getFirstName() + " " + patientResponse.getLastName())
                .appointmentDate(request.appointmentDate())
                .appointmentTime(request.appointmentTime())
                .location(doctorResponse.getLocation())
                .reason(request.reason())
                .build();
    }

    private AppointmentResponse toResponse(Appointment appointment) {
        return AppointmentResponse.builder()
                .doctorName(appointment.doctorName())
                .patientName(appointment.patientName())
                .appointmentDate(appointment.appointmentDate().toString())
                .appointmentTime(appointment.appointmentTime().toString())
                .location(appointment.location())
                .message(appointment.reason())
                .build();
    }
}
