# DropIt API Test

## Description

This project is a suite of API tests designed to verify the functionality of an online pet store, using RestAssured and Spring Boot. The tests cover creating, updating, and searching for pets based on their status.


## Dependencies

This project includes the following dependencies:

- **Spring Boot**: to configure the application.
- **RestAssured**: for RESTful API testing.
- **JUnit 5**: for writing and running tests.
- **Lombok**: to simplify working with Java classes.

## Environment Setup

1. Make sure you have [Java JDK 17+](https://www.oracle.com/java/technologies/javase-jdk17-downloads.html) and Maven installed.
2. Clone the repository:
```bash
   git clone https://github.com/your-repository/dropit-api-test.git
```
3. Navigate to the project directory:
```bash
   cd dropit-api-test
```
## Configuration

Create an application.properties file in the src/main/resources directory and add the following:
```properties
base-url=https://petstore.swagger.io/v2
```


## Running Tests

To execute the tests, run the following Maven command:

```bash
mvn clean test
```

