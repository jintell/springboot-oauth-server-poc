package com.jade.platform.patients.web;

import com.jade.platform.patients.dto.PatientRecordRequest;
import com.jade.platform.patients.dto.PatientRecordResponse;
import com.jade.platform.patients.service.PatientService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/v1/patients")
@RequiredArgsConstructor
public class PatientWeb {
    private final PatientService patientService;

    @PostMapping("/register")
    public Mono<ResponseEntity<PatientRecordResponse>> register(@Validated @RequestBody PatientRecordRequest request) {
        return patientService.recordPatient(request)
                .map(ResponseEntity::ok);
    }

    @GetMapping("{id}")
    public Mono<ResponseEntity<PatientRecordResponse>> getPatient(@PathVariable("id") Integer id) {
        return patientService.getPatient(id)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/email/{email}")
    public Mono<ResponseEntity<PatientRecordResponse>> getPatient(@PathVariable("email") String email) {
        return patientService.getPatient(email)
                .map(ResponseEntity::ok);
    }

    @GetMapping
    public Mono<ResponseEntity<List<PatientRecordResponse>>> getPatient() {
        return patientService.getAllPatients()
                .collectList()
                .map(ResponseEntity::ok);
    }

}
