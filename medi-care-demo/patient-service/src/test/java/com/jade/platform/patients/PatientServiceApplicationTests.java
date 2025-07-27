//package com.jade.platform.patients;
//
//import org.jade.platform.proto.definition.PatientDetailsRequest;
//import org.jade.platform.proto.definition.PatientResponse;
//import org.jade.platform.proto.definition.PatientServiceGrpc;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.grpc.client.GrpcChannelFactory;
//
//@SpringBootTest(classes = {PatientServiceApplicationTests.TestConfig.class})
//@EnableAutoConfiguration
//class PatientServiceApplicationTests {
//
//    @Autowired
//    PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub;
//
//    @Test
//    void getPatientDetailsTest() {
//        PatientResponse response
//                = patientServiceBlockingStub.getPatientDetail(PatientDetailsRequest.newBuilder().setId(1).build());
//        Assertions.assertNotNull(response, "Patient response should not be null");
//    }
//
//    @TestConfiguration
//    static class TestConfig {
//        @Bean
//        PatientServiceGrpc.PatientServiceBlockingStub patientServiceBlockingStub(GrpcChannelFactory channels) {
//            return PatientServiceGrpc.newBlockingStub(channels.createChannel("0.0.0.0:9091"));
//        }
//    }
//
//}
