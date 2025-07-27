package com.jade.platform.doctors.repository;

import com.jade.platform.doctors.model.Doctor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface DoctorRepository extends ReactiveCrudRepository<Doctor, Integer> {
    Mono<Doctor> findByEmail(String email);
}
