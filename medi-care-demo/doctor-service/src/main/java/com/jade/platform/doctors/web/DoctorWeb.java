package com.jade.platform.doctors.web;

import com.jade.platform.doctors.dto.DoctorRecordRequest;
import com.jade.platform.doctors.dto.DoctorRecordResponse;
import com.jade.platform.doctors.service.DoctorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doctors")
@RequiredArgsConstructor
public class DoctorWeb {
    private final DoctorService doctorService;

    @PostMapping("/register")
    public Mono<ResponseEntity<DoctorRecordResponse>> register(@Validated @RequestBody DoctorRecordRequest request) {
        return doctorService.addDoctor(request).map(ResponseEntity::ok);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<DoctorRecordResponse>> getDoctor(@PathVariable Integer id) {
        return doctorService.getDoctor(id)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<DoctorRecordResponse>> getDoctor(@PathVariable String email) {
        return doctorService.getDoctor(email)
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }

    @GetMapping
    public Mono<ResponseEntity<List<DoctorRecordResponse>>> getDoctor() {
        return doctorService.getDoctor()
                .collectList()
                .map(ResponseEntity::ok)
                .switchIfEmpty(Mono.just(ResponseEntity.notFound().build()));
    }
}
