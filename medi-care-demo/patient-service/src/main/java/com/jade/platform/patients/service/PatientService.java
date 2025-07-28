package com.jade.platform.patients.service;

import com.jade.platform.patients.dto.PatientRecordRequest;
import com.jade.platform.patients.dto.PatientRecordResponse;
import com.jade.platform.patients.model.Patient;
import com.jade.platform.patients.repository.PatientRepository;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.jade.platform.proto.definition.PatientRequestWithEmail;
import org.jade.platform.proto.definition.PatientRequestWithId;
import org.jade.platform.proto.definition.PatientResponse;
import org.jade.platform.proto.definition.PatientServiceGrpc;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

@Service
@RequiredArgsConstructor
public class PatientService extends PatientServiceGrpc.PatientServiceImplBase {
    private final PatientRepository repository;

    public Mono<PatientRecordResponse> recordPatient(PatientRecordRequest request) {
        return repository.save(toPatientModel(request))
                .map(patient -> PatientRecordResponse.builder()
                        .patientId(patient.getId())
                        .build());
    }

    public Mono<PatientRecordResponse> getPatient(Integer patientId) {
        return repository.findById(patientId).map(this::toPatientRecord);
    }

    public Mono<PatientRecordResponse> getPatient(String email) {
        return repository.findByEmail(email).map(this::toPatientRecord);
    }

    public Flux<PatientRecordResponse> getAllPatients() {
        return repository.findAll().map(this::toPatientRecord);
    }

    @Override
    public void getPatientDetail(PatientRequestWithId request, StreamObserver<PatientResponse> responseObserver) {
        repository.findById(request.getId())
                .map(patient -> PatientResponse.newBuilder()
                        .setFirstName(patient.firstName())
                        .setLastName(patient.lastName())
                        .setEmail(patient.email())
                        .setPhone(patient.phone())
                        .setAddress(patient.address())
                        .build())
                .switchIfEmpty(Mono.just( PatientResponse.newBuilder().build() ))
                .doOnNext(response -> processResponse(response, responseObserver))
                .doFinally(signal -> responseObserver.onCompleted())
                .subscribe();
    }

    @Override
    public void getPatientDetailByEmail(PatientRequestWithEmail request, StreamObserver<PatientResponse> responseObserver) {
        repository.findByEmail(request.getEmail())
                .map(patient -> PatientResponse.newBuilder()
                        .setFirstName(patient.firstName())
                        .setLastName(patient.lastName())
                        .setEmail(patient.email())
                        .setPhone(patient.phone())
                        .setAddress(patient.address())
                        .build())
                .switchIfEmpty(Mono.just( PatientResponse.newBuilder().build() ))
                .doOnNext(response -> processResponse(response, responseObserver))
                .doFinally(signal -> responseObserver.onCompleted())
                .subscribe();
    }

    private Patient toPatientModel(PatientRecordRequest request) {
        return Patient.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phone(request.phone())
                .email(request.email())
                .address(request.address())
                .build();
    }

    private PatientRecordResponse toPatientRecord(Patient patient) {
        return PatientRecordResponse.builder()
                .firstName(patient.firstName())
                .lastName(patient.lastName())
                .email(patient.email())
                .phone(patient.phone())
                .address(patient.address())
                .build();
    }

//    private PatientRecordResponse toResponse(Patient value){
//        Function<Patient, PatientRecordResponse> converter = patient -> PatientRecordResponse.builder()
//                .firstName(patient.firstName())
//                .lastName(patient.lastName())
//                .email(patient.email())
//                .phone(patient.phone())
//                .build();
//        return converter.apply(value);
//    }


    private <T> void processResponse(T value , StreamObserver<PatientResponse> responseObserver){
        if(Objects.nonNull(value) && checkEmpty(""+value)){
            try {
                responseObserver.onNext((PatientResponse) value);
            }catch (Exception e){
                System.err.println("Error here: "+e.getMessage());
            }
        }else {
            responseObserver.onError(Status.NOT_FOUND.withDescription("Patient not found").asRuntimeException() );
        }
    }

    private <T> boolean checkEmpty(T data) {
        Predicate<T> isNotBlank = t -> t instanceof String && !((String) t).trim().isBlank();
        Predicate<T> isNotEmpty = t -> t instanceof String && !((String) t).trim().isEmpty();
        return isNotEmpty.and(isNotBlank).test(data);
    }

}
