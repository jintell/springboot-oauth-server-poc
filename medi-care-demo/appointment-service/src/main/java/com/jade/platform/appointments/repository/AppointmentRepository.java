package com.jade.platform.appointments.repository;

import com.jade.platform.appointments.model.Appointment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface AppointmentRepository extends ReactiveCrudRepository<Appointment, Integer> {
    Flux<Appointment> findByDoctorId(String doctorId);
    Flux<Appointment> findByPatientId(String patientId);
    Flux<Appointment> findByAppointmentDate(LocalDate appointmentDate);
}
