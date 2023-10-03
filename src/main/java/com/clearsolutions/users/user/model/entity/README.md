# Test project for ClearSolution

## Requirements:
1. It has the following fields:
   - Email (required). Add validation against email pattern
   - First name (required)
   - Last name (required)
   - Birth date (required). Value must be earlier than current date
   - Address (optional)
   - Phone number (optional)


2. It has the following functionality:
   - Create user. It allows to register users who are more than [18] years old. The value [18] should be taken from properties file.
   - Update one/some user fields
   - Update all user fields
   - Delete user
   - Search for users by birth date range. Add the validation which checks that “From” is less than “To”.  Should return a list of objects


3. Code is covered by unit tests using Spring


4. Code has error handling for REST


5. API responses are in JSON format


6. Use of database is not necessary


7. Latest version of Spring Boot. Java version of your choice

## How to Run the Project

To run the project locally, follow these steps:

#### Running with JDK:

1. Clone the repository to your local machine: ``` git clone https://github.com/PanfiDen/Users```
2. Make sure you have Java and Maven installed.
3. To ensure the proper functioning of the application, you need to set the following Environment variables
   (These variables must be configured with the appropriate values for the H2 database for the program to work correctly.):
- DB_LOGIN=exapleDB_LOGIN;
- DB_PASSWORD=exapleDB_PASSWORD.
4. Open a terminal and navigate to the project's root directory.
5. Run the following command to build the project: `mvn clean install`.
6. Once the build is successful, run the following command to start the application: `mvn spring-boot:run`.
7. The application will start running on `http://localhost:8081`.
8. Access the application using a Postman or any other util.

