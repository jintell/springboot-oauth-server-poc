//package com.jade.platform.doctors;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.grpc.client.GrpcChannelFactory;
//
//@SpringBootTest(classes = {DoctorServiceApplicationTests.TestConfig.class})
//@EnableAutoConfiguration
//class DoctorServiceApplicationTests {
//
//    @Test
//    void contextLoads() {
//    }
//
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        DoctorServiceGrpc.DoctorServiceBlockingStub
//        DoctorServiceGrpc.D  .PatientServiceBlockingStub patientServiceBlockingStub(GrpcChannelFactory channels) {
//            return PatientServiceGrpc.newBlockingStub(channels.createChannel("0.0.0.0:9091"));
//        }
//    }
//
//}
//
//
//
