# Tododo API

## Running the project using ./mvnw 

Before starting up the server (without using docker), there are a few setup needs to be done: 

- Start up your mysql server on your local machine and use your credentials to login to the server.

- Create a new database with name `spring_security` using the command:

        CREATE DATABASE spring_security;

Then, head over to `application.properties` file (locates in the main/resources folder)

Uncomment the following lines:

    spring.datasource.url=jdbc:mysql://localhost:3306/spring_security
    spring.datasource.username=<your_mysql_username>
    If you set a password for your mysql server, add the password in this line:
    spring.datasource.password=<your_password>

and then comment out the following lines:
    spring.datasource.url=jdbc:mysql://tododo-db:3306/spring_security?autoReconnect=true&failOverReadOnly=false&maxReconnects=10
    spring.datasource.username=admin (leave this line if your username for the your local mysql server is also `admin`)

*Explanation: Basically what we done above is changing the connections of the spring boot application and the database server. The initial setup was used for running the project in docker, so both the API server and MySQL database are containerized. That's why we needed the line `spring.datasource.url=jdbc:mysql://tododo-db:3306/spring_security?autoReconnect=true&failOverReadOnly=false&maxReconnects=10` as this one point to the `tododo-db` database container. The credential setup to access this database are 'admin' and 'root' for username and password respectively. When using the ./mvnw command to run the project without docker, the api server needs to be connected to the available mysql database. of course you can still leave the configuration with the connection to the database inside the container, as well as the credentials attatched to that database, but the precondition is the database container has to be running for you to be able to access it. 
    
Then simply run the following command to start the server:

```bash
./mvnw spring-boot:run
```

The server runs in port 5055

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

There are also seed database for Todo and Note models, as these seed are necessary for the Hybrid application to have some initial data to show

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
