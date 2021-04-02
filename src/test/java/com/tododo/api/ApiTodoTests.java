package com.tododo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.AuthenticationRequest;
import com.tododo.api.models.Todo;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.TodoRepository;
import com.tododo.api.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Map;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTodoTests {

    private MockMvc mockMvc;

    private String accessToken;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        getAccessToken();

    }

    @Test
    void getTodosForUser() throws Exception {
        String url = "/api/todos";
        MvcResult forbiddenResult = mockMvc.perform(get(url)).andReturn();
        assertEquals(401, forbiddenResult.getResponse().getStatus());
        MvcResult result = mockMvc.perform(get(url).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void getTodoItem() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Todo newTodo = new Todo("Todo1", "Test todo1");
        newTodo.setUser(currentUser);
        int id = todoRepository.save(newTodo).getId();

        String url = "/api/todos/{id}";
        MvcResult result = mockMvc.perform(get(url, id).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addTodoItem() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Todo newTodo = new Todo("Todo1", "Test todo1");
        newTodo.setUser(currentUser);
        String jsonRequest = mapper.writeValueAsString(newTodo);
        String url = "/api/todos";
        MvcResult result = mockMvc.perform(post(url).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void deleteTodoItem() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Todo newTodo = new Todo("Todo1", "Test todo1");
        newTodo.setUser(currentUser);
        int id = todoRepository.save(newTodo).getId();

        String url = "/api/todos/{id}";
        MvcResult result = mockMvc.perform(delete(url, id).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals("Your request has been successfully handled!", result.getResponse().getContentAsString());
    }

    private void getAccessToken() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest();
        request.setUsername("tien@email.com");
        request.setPassword("Tien12345");
        String jsonRequest = mapper.writeValueAsString(request);

        MvcResult result = mockMvc
                .perform(post("/api/authenticate").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        String resultString = result.getResponse().getContentAsString();

        Map<String, String> jwt = mapper.readValue(resultString, Map.class);
        accessToken = jwt.get("jwt");

        // accessToken = rsp.getJwt();
    }

}
