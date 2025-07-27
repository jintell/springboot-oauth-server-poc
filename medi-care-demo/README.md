# The Medical Care Health Application

### PROJECT STRUCTURE OVERVIEW
```
medi-care-demo/
├── proto-definition/
│   ├── src/main/proto (All the service .proto files are stored here)
│   └── common-proto/ (gRPC Protobuf Definitions)
├── patient-service/
│   └── api-services/ (Spring Boot WebFlux + gRPC server + Spring R2DBC + Postgres)
├── doctor-service/
│   └── api-services/ (Spring Boot WebFlux + gRPC server + Spring R2DBC + Postgres)
├── appointment-service/
│   └── api-services/ (Spring Boot WebFlux + gRPC server + Spring R2DBC + Postgres)
├── README.md
```
### TECHNOLOGY USED
#### Root Project
- settings.gradle (contains all the subprojects)

#### Proto Definition
- Protobuf 3.x
- gRPC 1.x

#### Patient Service
- Spring Boot 3.x + WebFlux
- Spring Boot gRPC Server (via yidongnan/grpc-spring-boot-starter)
- PostgresSQL
- Lombok
- Proto-Definition

#### Doctor Service
- Spring Boot 3.x + WebFlux
- Spring Boot gRPC Server (via yidongnan/grpc-spring-boot-starter)
- PostgresSQL
- Lombok
- Proto-Definition
- n
- #### Appointment Service
- Spring Boot 3.x + WebFlux
- Spring Boot gRPC Server (via yidongnan/grpc-spring-boot-starter)
- PostgresSQL
- Lombok
- Proto-Definition

### CORE FEATURES
1. Patient/Doctor/Appointment Tracking
    - CRUD for patients, doctors, appointments
2. Appointment Management
    - CRUD for appointments
    - Booking an appointment (communicates via the gRPC stub to fetch the doctor's and patient's availability)
    - Canceling an appointment (Not implemented)

### Examples
Before running the application, make sure you have the following:
#### Backend application
1. Set the environment variables in the `HOST_DB`, `HOST_USER`, and `HOST_PASSWORD` for database connection.
2. Navigate to the `med-cate-demo` directory
3. from the terminal run: `./gradlew clean build` or `./gradlew clean build --info`
4. Navigate to the `med-cate-demo/patient-service` directory and run the application: `./gradlew bootRun`
5. Navigate to the `med-cate-demo/doctor-service` directory and run the application: `./gradlew bootRun`
6. Navigate to the `med-cate-demo/appointment-service` directory and run the application: `./gradlew bootRun`
