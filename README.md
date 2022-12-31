# user-access-control
The user access control module is responsible for managing the users and their roles for controlling the access to each module at PIDS@Stop.

### Requirements 
For building and running the application you need:

- JDK 1.11
- Maven 2.4.1
- PostgreSQL

Update the database configurations in ``/src/main/resources/application.properties``

### Running the application

Install the IDE (preferred IDE: IntelliJ IDEA) & run the main method from ``/src/main/java/test/students/StudentsApplication.java``. <br/>
Alternatively, you can also run the following command from the terminal.

```shell
mvn spring-boot:run
```
The application is accessible on ``localhost:9999``

The application is accessible on Swaager Url "http://localhost:9999/swagger-ui/index.html#/"