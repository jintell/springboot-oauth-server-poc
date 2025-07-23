# The Stock Management Application

### DESCRIPTION
This is a simple stock management application that allows users to add, update, and retrieve stock insights.
The application uses GraphQL to expose real-time stock price updates.
The application also created a well abstracted rest-to-graphql and graphql-to-rest resolver.

For example, you can run a rest request with selected fields in the response payload:

`curl 'http://localhost:9100/v1/stocks/bac/insight/metrics?q=symbol%2Cname%2CmarketCap%2CpeRatio%2CrevenueGrowth'`

and the response will be:

```
{
    "symbol": "BAC",
    "name": "Bank of America Corporation",
    "marketCap": 3.5E7,
    "peRatio": 14.02,
    "revenueGrowth": 4.73
}
```

### PROJECT STRUCTURE OVERVIEW
```
gl-rest-app/
├── backend/
│   ├── api-service/ (Spring Boot WebFlux + GraphQL + Subscription)
│   └── docker-compose.yml
├── frontend/
│   └── react-app/ (React + Apollo Client)
├── README.md
```
### TECHNOLOGY USED
#### Backend
- Spring Boot 3.x + WebFlux
- Spring GraphQL + GraphQL Subscriptions using WebSockets
- Spring R2dbc Data 
- Lombok
- PostgreSQL

#### Frontend
- React 18
- Apollo Client 3.x (GraphQL queries + subscriptions)
- TypeScript (recommended for type safety)

### CORE FEATURES
1. Real-time Market Stock Price Subscription
    - Exposed via GraphQL Subscription
2. Stock Management
    - Adding/Updating/Retrievals and subscription

### Examples
Before running the application, make sure you have the following:
#### Backend application
1. Build the project and exclude the tests: `./gradlew clean build -x test`
2. Set the environment variable for the DB and the Run the application: `./gradlew bootRun`

#### Frontend application
1. Navigate to the `frontend` directory
2. Install the dependencies: `npm install`
3. Run the application: `npm start`