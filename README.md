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

## Testing the Application

To test the application, run the following command in the terminal:

```bash
mvn test
```
Please note that running these tests will clear all data from `mydatabase` due to the embedded nature and set-up of some of the tests. For this reason, I recommend playing around with the initial data I have added to the db first to get an idea of functionality before running the test suite. 

## Future Improvements

If I had more time, or the scope of the assignment was widened, there are several changes I would make to my implementation.

### Sharding

I would shard my database due to the large volume of documents to distribute the load across multiple servers so as to improve efficiency. For instance, I could shard by area code.

### Replication

I would implement a replication strategy so as to aid in the availability and disaster recovery of my database to name a few benefits.

### Bulk Write Operations

Where possible, I would use bulk write operations to reduce the number of roundtrips between my application and Mongo.

### MDN/Email Storage

I would alter how an MDN is stored internally to improve efficiency and allow type-specific operations. For instance, I might create a class `MDN` that parses the input string and stores, as separate integers, the 3 components of a US phone number: area code, central office code, and line number. As an example, this could improve efficiency if you wanted to look up a user by the area code of their MDN. I might also create an `Email` class that parses the input string and stores the username and domain as separate strings. As an example, this could improve efficiency when querying a user by their email. 

### Expand Endpoints

I would add more endpoints to expand the functionality of my microservice. For instance, I could add functionality to search a user by MDN. 

### Improve HTML Forms

I would modify the visual look of my HTML forms to make them look more professional and improve user experience. I did not know HTML before beginning this project and so I had to stick to the basics. 

### Testing

I would implement a more comprehensive test suite to ensure I have thoroughly tested my application. Additionally, I would create a separate db `test` that exists only for the purposes of embedded testing so that the data from `mydatabase` doesn't get wiped each time tests are run.

### Additional Improvements

Finally, I would implement secret credential management and also audit db changes through the use of logging. 

## What I Learned

Where do I begin? Before this project I had never used SpringBoot, implemented/designed a REST server, written HTML, written Swagger docs, used MongoDB, or written embedded DB tests. In a week, I taught myself how to do all of these things and implement a REST server that I am proud of (even though there are certainly improvements that can be made as listed above). Regardless of how this turns out with the job, I just wanted to add that I am very grateful for this experience to learn so much as this experience has strengthened my skills in new ways and will be of benefit to me either way. However, if this does work out, I am very much looking forward to learning so much on the job with USMobile! I hope I have demonstrated that I can learn new topics quickly and have the ability to self-teach. 
