# Spring Boot Application

## Description
This is a Spring Boot application designed for ease of use and deployment. It provides a set of RESTful endpoints for interacting with the system and includes features such as a Dockerfile for containerization and OpenAPI documentation for easy API exploration.

## Prerequisites
- Java 11 or later
- Maven 3.6.0 or later
- Docker (optional, if you wish to run the application in a container)

## How to Run
1. **Clone the Repository**
   -Repository url: https://github.com/redigongo/exchangeApp.git

2. **Build the Application**
   Run the following command to build the application:
   ```bash
   mvn clean install
   ```

3. **Run the Application**
   Execute the following command to start the application:
   ```bash
   mvn spring-boot:run
   ```
   The application will start on `http://localhost:8080`.

4. **Using Docker (Optional)**
   To build and run the application using Docker:
    - Build the Docker image:
      ```bash
      docker build -t spring-boot-app .
      ```
    - Run the Docker container:
      ```bash
      docker run -p 8080:8080 spring-boot-app
      ```

## OpenAPI Documentation
The application provides OpenAPI documentation for easy exploration of the available endpoints. Access it by navigating to the following URL in your browser:

```
http://localhost:8080/swagger-ui.html
```

## Endpoints
The application provides the following endpoints:

1. **POST /api/exchange/rate**
    - **Description:** Returns the currency between 2 given rates

2. **POST /api/exchange**
    - **Description:** Converts the amount with the latest rate.

3. **GET /api/transaction/{id}**
    - **Description:** Returns the transaction matching the id. 

4. **POST /api/transaction/filter**
    - **Description:** Returns all/filtered transactions.

## Notes
- Endpoints can be tested through swagger-ui and examples are provided.
- The application requires **no authorization**.
- Ensure that port `8080` is available on your local machine or containerized environment.


