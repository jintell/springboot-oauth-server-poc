package com.jade.platform.doctors.service;

import com.jade.platform.doctors.dto.DoctorRecordRequest;
import com.jade.platform.doctors.dto.DoctorRecordResponse;
import com.jade.platform.doctors.model.Doctor;
import com.jade.platform.doctors.repository.DoctorRepository;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.jade.platform.proto.definition.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class DoctorService extends DoctorServiceGrpc.DoctorServiceImplBase {
    private final DoctorRepository doctorRepository;

    public Mono<DoctorRecordResponse> addDoctor(DoctorRecordRequest request) {
        return doctorRepository.save(toModel(request))
                .map(doctor -> DoctorRecordResponse.builder().doctorId(doctor.id()).build());
    }

    public Mono<DoctorRecordResponse> getDoctor(Integer id) {
        return doctorRepository.findById(id)
                .map(this::toResponse);
    }

    public Mono<DoctorRecordResponse> getDoctor(String email) {
        return doctorRepository.findByEmail(email)
                .map(this::toResponse);
    }

    public Flux<DoctorRecordResponse> getDoctor() {
        return doctorRepository.findAll()
                .map(this::toResponse);
    }

    @Override
    public void getDoctorDetail(DoctorDetailsRequest request, StreamObserver<DoctorResponse> responseObserver) {
        doctorRepository.findById(request.getId())
                .map(doctor -> DoctorResponse.newBuilder()
                        .setFirstName(doctor.firstName())
                        .setLastName(doctor.lastName())
                        .setEmail(doctor.email())
                        .setPhone(doctor.phone())
                        .setSpecialty(doctor.specialty())
                        .setLocation(doctor.location())
                        .setCenterName(doctor.centerName())
                        .build())
                .switchIfEmpty(Mono.just(DoctorResponse.newBuilder().build()))
                .doOnNext(response -> processResponse(response, responseObserver))
                .doFinally(signalType -> responseObserver.onCompleted())
                .subscribe();
    }

    @Override
    public void getDoctorDetailWithEmail(DoctorDetailsEmailRequest request, StreamObserver<DoctorResponse> responseObserver) {
        doctorRepository.findByEmail(request.getEmail())
                .map(doctor -> DoctorResponse.newBuilder()
                        .setFirstName(doctor.firstName())
                        .setLastName(doctor.lastName())
                        .setEmail(doctor.email())
                        .setPhone(doctor.phone())
                        .setSpecialty(doctor.specialty())
                        .setLocation(doctor.location())
                        .setCenterName(doctor.centerName())
                        .build())
                .switchIfEmpty(Mono.just(DoctorResponse.newBuilder().build()))
                .doOnNext(response -> processResponse(response, responseObserver))
                .doFinally(signalType -> responseObserver.onCompleted())
                .subscribe();
    }

    private Doctor toModel(DoctorRecordRequest record) {
        return Doctor.builder()
                .firstName(record.firstName())
                .lastName(record.lastName())
                .email(record.email())
                .phone(record.phone())
                .location(record.location())
                .centerName(record.centerName())
                .specialty(record.specialty())
                .build();
    }

    private DoctorRecordResponse toResponse(Doctor doctor) {
        return DoctorRecordResponse.builder()
                .firstName(doctor.firstName())
                .lastName(doctor.lastName())
                .email(doctor.email())
                .phone(doctor.phone())
                .specialty(doctor.specialty())
                .centerName(doctor.centerName())
                .location(doctor.location())
                .build();
    }

    private <T> void processResponse(T value , StreamObserver<DoctorResponse> responseObserver){
        if(Objects.nonNull(value) && !checkEmpty(value)){
            try {
                responseObserver.onNext((DoctorResponse) value);
            }catch (Exception e){
                System.err.println("Error here: "+e.getMessage());
            }
        }else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Doctor not found").asRuntimeException() );
        }
    }

    private <T> boolean checkEmpty(T data) {
        String dataCheck = ""+data;
        return dataCheck.trim().isBlank();
    }
}
