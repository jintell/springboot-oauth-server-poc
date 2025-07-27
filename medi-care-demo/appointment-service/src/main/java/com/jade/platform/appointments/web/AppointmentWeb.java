package com.jade.platform.appointments.web;

import com.jade.platform.appointments.constants.AppointmentViewType;
import com.jade.platform.appointments.dto.AppointmentRequest;
import com.jade.platform.appointments.dto.AppointmentResponse;
import com.jade.platform.appointments.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/appointments")
@RequiredArgsConstructor
public class AppointmentWeb {
    private final AppointmentService service;

    @PostMapping("/book")
    public Mono<ResponseEntity<AppointmentResponse>> bookAppointment(
            @Validated @RequestBody AppointmentRequest request) {
        return service.bookAppointment(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/{email}")
    public Mono<ResponseEntity<List<AppointmentResponse>>> getAppointment(
            @PathVariable String email,
            @RequestParam AppointmentViewType viewType) {
        return service.viewAppointments(email, viewType)
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/date/{date}")
    public Mono<ResponseEntity<List<AppointmentResponse>>> getAppointmentByDate(
            @PathVariable LocalDate date) {
        return service.viewAppointments(date)
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
            }
}
