# Tododo API

To start up the server:

```bash
./mvnw spring-boot:run
```
Swagger documentation could be found at:

`http://localhost:5055/v2/api-docs` - swagger2 raw json version

`http://localhost:5055/swagger-ui/index.html` - swagger ui (prettier version)

## Start up

### Authentication and authorization flow

1. Login 
    - With login, please use the seed credentials provided in the UserController. They are

        ```java
        {
        	"username":"tienthai",
        	"password": "Tien12345"
        }
        ```

        Login Url: POST `https://localhost:5055/api/authenticate`

        Sample request body:

        - Using postman: Select *Body,*  choose raw
        - enter the above credentials with json format in the body
        - Add `Content-Type` as Key and `application/json` as Value in *Headers* tab

        *Response body:* 

        ```java
        200 OK 
        accessToken: <token>
        ```

2. Register 
    - Register new user

        Register Url: POST `https://localhost:5055/api/register`

        Sample request body:

        - Using postman: Select *Body,*  choose raw
        - enter the above credentials with json format in the body
        ```java
        {
        	"username": <string:your_user_name>,
        	"password": <string:your_secured_password>
        }
        ```
        - Add `Content-Type` as Key and `application/json` as Value in *Headers* tab

        *Response body:* 

        ```java
        200 OK 
        {
            "id": 123,
            "username": "newUser",
            "password": "$2a$10$sE1wcSEjBu2dEcjb91RADOMKywSPJB3Io4K6VuZXQPhZDzZ7zYu5C",
            "active": true,
            "role": "user"
        }
        ```