# Tododo API

To start up the server:

```bash
./mvnw spring-boot:run
```
Swagger documentation could be found at:

`http://localhost:8080/v2/api-docs` - swagger2 raw json version

`http://localhost:8080/swagger-ui/index.html` - swagger ui (prettier version)

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

        Login Url: POST `https://localhost:8080/api/authenticate`

        Sample request body:

        - Using postman: Select *Body,*  choose raw
        - enter the above credentials with json format in the body
        - Add `Content-Type` as Key and `application/json` as Value in *Headers* tab

        *Response body:* 

        ```java
        200 OK 
        accessToken: <token>
        ```