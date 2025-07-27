package com.jade.platform.appointments.config;

import org.jade.platform.proto.definition.DoctorServiceGrpc;
import org.jade.platform.proto.definition.PatientServiceGrpc;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.grpc.client.GrpcChannelFactory;

@Configuration
public class AppointmentGrpcStub {

    @Bean
    DoctorServiceGrpc.DoctorServiceBlockingStub doctorServiceBlockingStub(GrpcChannelFactory channel) {
        return DoctorServiceGrpc.newBlockingStub(channel.createChannel("doctorService"));
    }

    @Bean
    PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub(GrpcChannelFactory channel) {
        return PatientServiceGrpc.newBlockingStub(channel.createChannel("patientService"));
    }
}
