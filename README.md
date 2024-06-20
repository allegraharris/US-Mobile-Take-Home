# SpringBoot Java REST Server Microservice Design

This project is a Spring Boot application written in Java, using Maven as a build tool. The application provides a service for managing cycles, daily usage, and users. The service is designed around the following key components:

## Entities

### Cycle

`Cycle` is an entity class that represents a cycle. It contains fields `id`, `mdn`, `startDate`, `endDate`, and `userId`.

### DailyUsage

`DailyUsage` is an entity class that represents a daily usage record. It contains fields `id`, `mdn`, `usageDate`, `usedInMb`, and `userId`.

### User

`User` is an entity class that represents a user. It contains fields `id`, `mdn`, `firstName`, `lastName`, `email`, and `password`. There is a 1-1 relationship between a user and an mdn. 

## Repositories

### CycleRepository

`CycleRepository` is an interface that extends `MongoRepository`. It provides methods for performing CRUD operations on the `Cycle` entities stored in the database.

### DailyUsageRepository

`DailyUsageRepository` is an interface that extends `MongoRepository`. It provides methods for performing CRUD operations on the `DailyUsage` entities stored in the database.

### UserRepository

`UserRepository` is an interface that extends `MongoRepository`. It provides methods for performing CRUD operations on the `User` entities stored in the database.

## Services

### CycleService

`CycleService` is a service class that provides methods for managing cycles. It interacts with the `CycleRepository` and `UserRepository` to perform operations such as adding, deleting, and retrieving cycles.

### DailyUsageService

`DailyUsageService` is a service class that provides methods for managing daily usage records. It interacts with the `DailyUsageRepository`, `UserRepository` and `CycleService` to perform operations such as adding, updating, and retrieving daily usage records.

### UserService

`UserService` is a service class that provides methods for managing users. It interacts with the `UserRepository`, `CycleRepository` and `DailyUsageRepository` to perform operations such as adding, updating, and retrieving users.

## Testing

The service is tested using JUnit, Mockito, WebMvcTest, and DataMongoTest. The tests include embedded MongoDB tests as well as controller tests. The tests cover various scenarios such as adding a cycle, deleting a cycle, retrieving all cycles, retrieving cycle history, and retrieving the most recent cycle. Similar tests are also performed for daily usage records and users.

## Database

The application uses MongoDB as its database. The MongoDB instance is run as a Docker container, as defined in the `compose.yaml` file. The MongoDB instance is initialized with a database named `mydatabase` and a root user with username `root` and password `secret`.

## Running the Application

To run the application, you need to have Docker installed. You can start the MongoDB instance by running the following command in the terminal:

```bash
docker-compose -f compose.yaml up -d
```

Then, you can run the Spring Boot application by running the following command in the terminal:

```bash
mvn spring-boot:run
```

The application will then be accessible at `http://localhost:8080/home`.

Please note that you need to have Maven installed to run the above command. 
