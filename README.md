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

        Login Url: POST `http://localhost:5055/api/authenticate`

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

        Register Url: POST `http://localhost:5055/api/register`

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
3. Test Get request
    - Receive "Hello user!" as response

        Test URL: GET `http://localhost:5055/api/hello`

        Before adding authorization header, you are expected to receive response with code 401-Unauthorized.

        - In Postman, choose Authorization type as Bearer Token if using Authorization tab. If using Headers, specify Key="Authorization" and Value=Bearer <your_access_token> (**space** between `Bearer` and `access_token`)

        *Note: To retrieve access_token, you need to authenticate yourself by providing the valid credentials and follow Section 1 above

        *Response body:* 

        ```json
        200 OK 
        Hello user!
        ```
