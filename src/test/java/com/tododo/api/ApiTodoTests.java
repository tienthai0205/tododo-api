package com.tododo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.AuthenticationRequest;
import com.tododo.api.models.Tag;
import com.tododo.api.models.Todo;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.TagRepository;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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
    private TagRepository tagRepository;

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
    }

    @Test
    void getTodoItem() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Todo newTodo = new Todo("Todo1", "Test todo1");
        newTodo.setUser(currentUser);
        int id = todoRepository.save(newTodo).getId();

        String url = "/api/todos/{id}";
        MvcResult badResult = mockMvc.perform(get(url, 30).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(404, badResult.getResponse().getStatus());
        assertEquals("Todo item with id 30 not found", badResult.getResponse().getContentAsString());

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Todo maxTodo = new Todo("Todo10", "Test todo10");
        maxTodo.setUser(anotherUser);
        int maxTodoId = todoRepository.save(maxTodo).getId();
        MvcResult forbiddenResult = mockMvc
                .perform(get(url, maxTodoId).header("Authorization", "Bearer " + accessToken)).andReturn();

        assertEquals(403, forbiddenResult.getResponse().getStatus());

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

        MvcResult badResult = mockMvc.perform(delete(url, 30).header("Authorization", "Bearer " + accessToken))
                .andReturn();

        assertEquals(404, badResult.getResponse().getStatus());
        assertEquals("Todo item with id 30 not found", badResult.getResponse().getContentAsString());

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Todo maxTodo = new Todo("Todo10", "Test todo10");
        maxTodo.setUser(anotherUser);
        int maxTodoId = todoRepository.save(maxTodo).getId();
        MvcResult forbiddenResult = mockMvc
                .perform(delete(url, maxTodoId).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(403, forbiddenResult.getResponse().getStatus());

        MvcResult result = mockMvc.perform(delete(url, id).header("Authorization", "Bearer " + accessToken))
                .andReturn();

        assertEquals("Your request has been successfully handled!", result.getResponse().getContentAsString());
    }

    @Test
    void addTagToTodo() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Todo newTodo = new Todo("Todo1", "Test todo1");
        newTodo.setUser(currentUser);
        int id = todoRepository.save(newTodo).getId();

        Tag newTag = new Tag("Tag1", "Test tag1 for todo1");
        newTag.setUser(currentUser);
        int tagId = tagRepository.save(newTag).getId();

        String url = "/api/todos/{id}/tag/{tagId}";
        MvcResult result = mockMvc.perform(put(url, id, tagId).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void removeTagFromTodo() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Todo newTodo = new Todo("Todo1", "Test todo1");
        newTodo.setUser(currentUser);
        int id = todoRepository.save(newTodo).getId();

        Tag newTag = new Tag("Tag3", "Test tag3 for todo1");
        newTag.setUser(currentUser);
        int tagId = tagRepository.save(newTag).getId();

        String url = "/api/todos/{id}/tag/{tagId}";
        MvcResult result = mockMvc.perform(delete(url, id, tagId).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getTags() throws Exception {
        String url = "/api/todos/{id}/tags";

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Todo maxTodo = new Todo("Todo10", "Test todo10");
        maxTodo.setUser(anotherUser);
        int maxTodoId = todoRepository.save(maxTodo).getId();

        MvcResult forbiddResult = mockMvc.perform(get(url, maxTodoId).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(403, forbiddResult.getResponse().getStatus());
        MvcResult result = mockMvc.perform(get(url, 1).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void updateTodoItem() throws Exception {
        String url = "/api/todos/{id}";
        Todo updateTodo = new Todo("Test update title", "test update description", 1000, 50.5f);
        String jsonRequest = mapper.writeValueAsString(updateTodo);

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Todo maxTodo = new Todo("Todo10", "Test todo10");
        maxTodo.setUser(anotherUser);
        int maxTodoId = todoRepository.save(maxTodo).getId();

        MvcResult forbiddenResult = mockMvc.perform(put(url, maxTodoId).content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(403, forbiddenResult.getResponse().getStatus());

        MvcResult notfoundResult = mockMvc.perform(put(url, 300).content(jsonRequest)
                .contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(404, notfoundResult.getResponse().getStatus());

        System.out.println(todoRepository.findAll());
        MvcResult result = mockMvc.perform(put(url, 1).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
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
