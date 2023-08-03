 # Example Employees Application
 
## Running the application
### IntelliJ
Open the Project in IntelliJ and navigate to
`src/main/java/com/example/employees/EmployeesApplication.java`

In the gutter, click the green play button to launch the Spring Boot application.

### Command line
If you are running the application from a command line, you can launch it with the gradle task:
`gradlew :bootRun`
or
`./gradlew :bootRun`
depending on your platform.

## Running the Tests
Again depending on your platform you can execute the test suite through gradle tasks. Either:
`gradlew :test` or `./gradlew :test`

## Application Startup
Upon startup, the application will dump some fixture data into the ephemeral storage. This is faciliated by
the ApplicationStartup listener component. If you want a clean store, disable this component via the boolean property:

`com.example.employees.load-fixture`

## Postman
A Postman collection is provided for interfacing with the API much in the same way a UI would. Be sure to import 
both the Environment and the Collection and select the Environment from the drop down in Postman. The Collection 
heavily relies on the environment variables for requests and random data creation.