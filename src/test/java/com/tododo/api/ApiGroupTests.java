package com.tododo.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.*;
import com.tododo.api.repositories.*;

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

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
public class ApiGroupTests {
    private MockMvc mockMvc;

    private String accessToken;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        getAccessToken();

    }

    @Test
    void getAllGroups() throws Exception {
        String url = "/api/groups/all";
        MvcResult result = mockMvc.perform(get(url).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getGroup() throws Exception {
        String url = "/api/groups/{id}";

        MvcResult notfoundResult = mockMvc.perform(get(url, 100).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(404, notfoundResult.getResponse().getStatus());
        assertEquals("Group with id 100 not found", notfoundResult.getResponse().getContentAsString());

        Group newGroup = new Group("Group test1", "This is a test group");
        int id = groupRepository.save(newGroup).getId();
        MvcResult result = mockMvc.perform(get(url, id).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addGroup() throws Exception {
        String url = "/api/groups";
        Group newGroup = new Group("Fangroup", "This is a fan group");
        System.out.println("+++++++++" + groupRepository.findAll());
        String jsonRequest = mapper.writeValueAsString(newGroup);

        MvcResult result = mockMvc.perform(post(url).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)).andReturn();
        System.out.println("+++++++++" + groupRepository.findAll());
        assertEquals(201, result.getResponse().getStatus());
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
