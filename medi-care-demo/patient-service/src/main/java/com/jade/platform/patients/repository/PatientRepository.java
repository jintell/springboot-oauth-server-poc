package com.jade.platform.patients.repository;

import com.jade.platform.patients.model.Patient;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PatientRepository extends ReactiveCrudRepository<Patient, Integer> {
    Mono<Patient> findByEmail(String email);
}
