# The Stock Management Application

### PROJECT STRUCTURE OVERVIEW
```
stock-market-app/
├── backend/
│   ├── api-service/ (Spring Boot WebFlux + GraphQL + Subscription)
│   ├── grpc-service/ (Spring Boot WebFlux + gRPC server)
│   ├── common-proto/ (gRPC Protobuf Definitions)
│   └── docker-compose.yml
├── frontend/
│   └── react-app/ (React + Apollo Client + gRPC-web if needed)
├── README.md
```
### TECHNOLOGY USED
#### Backend
- Spring Boot 3.x + WebFlux 
- Spring GraphQL + GraphQL Subscriptions using WebSockets 
- Spring Boot gRPC Server (via yidongnan/grpc-spring-boot-starter)
- PostgreSQL (or MongoDB for timeseries market data)
- Redis (for pub/sub market updates and caching)

#### Frontend
- React 18 
- Apollo Client 3.x (GraphQL queries + subscriptions)
- TailwindCSS or MUI (for UI)
- TypeScript (recommended for type safety)

### CORE FEATURES
1. Real-time Market Data Subscription 
   - Exposed via GraphQL Subscription or gRPC Stream 
2. User Management 
   - Login/Registration + JWT Authentication 
3. Subscription Plans 
   - Basic, Premium tiers controlling access level 
4. Portfolio Tracking 
   - CRUD for stocks user owns 
5. Admin Panel 
   - Managing available stock data, user subscriptions

### Examples
Before running the application, make sure you have the following:
#### Backend application
1. Build the project and exclude the tests: `./gradlew clean build -x test`
2. Set the environment variable for the DB and the Run the application: `./gradlew bootRun`

#### Frontend application
1. Navigate to the `frontend/stock-market-frontend` directory
2. Install the dependencies: `npm install`
3. Run the application: `npm start`