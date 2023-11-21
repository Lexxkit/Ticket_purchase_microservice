#### Description:
1) Implement microservices in the Java programming language and Spring framework to enable users to buy transportation tickets.
2) The task consists of a main task and additional tasks to show the degree of mastery of certain technologies. Completion of the additional tasks is optional, but will be a plus.
3) The implementation may allow for different variations. The purpose of the assignment is to show the level of mastery of technologies, not scrupulous adherence to the specification.
4) It is necessary to implement only the backend part (Java microservices). The frontend part does not need to be implemented.
5) Source code should be published on Github/Gitlab/Bitbucket.
6) Build - Maven or Gradle.

##### Main task:
1) Implement a microservice with a REST interface that will process ticket purchase requests.
2) Main entities:
   1. **Ticket** - the right to travel on a certain route on a certain date and seat in a vehicle. Attributes: route, date/time, seat number, price.
   2. **Route** - a specific path of travel. Attributes: origin, destination, carrier, duration in minutes.
   3. **Carrier** - the company providing the transportation. Attributes: name, telephone number.
   4. **User** - the entity making the ticket purchase. Attributes: login, password, full name.
3) Basic REST methods:
   1. New user registration (all user logins are unique).
   2. Retrieving a list of all tickets available for purchase, with the ability to paginate (page/size) and filter by the following attributes:
      1. Date/Time
      2. Departure/destination points (occurrence by row).
      3. Name of carrier (occurrence per line).
   3. Purchase of a specific ticket. A ticket already purchased should not be available for purchase again.
   4. Obtaining a list of all purchased tickets for the current user.
4) All data must be stored in PostgreSQL database.
5) It is not allowed to use Hibernate/Spring Data to access the database. Possible options are JDBC, JdbcTemplate, JooQ, etc.
6) Input data of REST methods must be validated. In case of validation error, HTTP error 400 should be returned, and an error message should be returned in the response body (you need to think of any response body format other than the standard Spring Web one).
7) Swagger documentation via annotations in the code is required.
8) Optional: possible to implement with Spring WebFlux instead of Spring Web.
9) Optional: write a Dockerfile to create a Docker image of the microservice

Additional Task 1
1) Implement user authentication using JWT tokens (access and refresh tokens along the lines of OAuth 2). Access token should be limited in validity time.
2) Add methods to authenticate already registered users and refresh access token by refresh token.
3) HTTP error 401 should be returned when calling basic methods with invalid token.

Additional task 2
1) Add a "Role" entity for users, which is subdivided into "Buyer" and "Administrator".
2) Add new REST methods to manage (add/modify/delete) the Ticket, Route and Carrier entities. These methods should only be available to users with the "Administrator" role.
3) For customers, calls to these methods should result in an HTTP error 403.

Additional task 3 *
1) Add caching of purchased tickets for each user in the Redis database.
2) When requesting purchased tickets for the current user, the list of tickets should be retrieved from Redis first. If the data is missing in Redis, the PostgreSQL database should be queried and the data updated in Redis.
3) When a ticket is purchased, the data in Redis should be updated.

Additional task 4 **
1) Add a new microservice that will take data about purchased tickets from Kafka topik and save it to a separate table in a database (the same PostgreSQL or any other database of your choice, e.g. NoSQL database).
2) When buying a ticket in the main microservice, the ticket data is additionally sent to this Kafka topic.