# Tododo API

To start up the server:

```bash
./mvnw spring-boot:run
```

To run all the test:

```
./mvnw test
```

The project uses Jacoco for checking code coverage. To view the report:
* if using the vscode test plugin for java, in order to generate the jacoco report, you have the run the following commands

```
./mvnw clean verify
./mvnw jacoco:report
```
* if using the ./mvnw test command, the jacoco report will be automatically created (because of the setup in pom.xml) so you would not have to run the any extra commands

Jacoco report is located at `target/site/jacoco/index.html`

Swagger documentation could be found at:

`http://localhost:5055/v2/api-docs` - swagger2 raw json version

`http://localhost:5055/swagger-ui/index.html` - swagger ui (prettier version)

## Database seeding

The seed data can be found in the file data.sql. 

    - username: tien@email.com, password: Tien12345 (admin)
    - username: max@email.com, password: Max12345 (admin)
    - username: user@email.com, password: User12345 (user)

Notice that there is no mechanisim to remove the table if already exist, therefore in order to prevent duplicate data in the database, after running the project for the first time and got the seed data in the database, please comment out the following line in the `application.properties`

    spring.datasource.initialization-mode=always

## Docker compose and Dockerfile

To run the project in docker, use the following command: (navigate to the folder that contain docker-compose file before running the command)

    docker-compose up

To check the database and see the actual data, you can either connect to a database client software or exec into the docker container using the following commands:

    docker exec -it tododo-db bash

Once you are inside the database container, use the mysql command to access the database

    mysql -u admin -p

After that, you would be prompted to enter password. The password is `root`. Enter the password and hit "Enter". Now you have access to the database! Just have to specify the database, from this moment onwards you can use mysql statements to query, delete, update the record. For example:

    mysql> use spring_security;
    mysql> select * from user;

