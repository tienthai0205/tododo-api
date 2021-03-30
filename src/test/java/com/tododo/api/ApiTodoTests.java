package com.tododo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.AuthenticationRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

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

    // @Test
    // void getTodoItem() throws Exception {
    // String url = "/api/todos/{id}";
    // MvcResult result = mockMvc.perform(get(url, "1").header("Authorization",
    // "Bearer " + accessToken)).andReturn();
    // assertEquals(200, result.getResponse().getStatus());
    // }

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
