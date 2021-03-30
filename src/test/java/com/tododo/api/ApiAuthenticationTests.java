package com.tododo.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.AuthenticationRequest;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.UserRepository;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.test.annotation.DirtiesContext.ClassMode;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DirtiesContext(classMode = ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class ApiAuthenticationTests {

    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    // @BeforeEach
    // public void setup() {
    // mockMvc = MockMvcBuilders.standaloneSetup(homeController).build();
    // }

    @Test
    void testRegisterNewUser() throws Exception {
        AuthenticationRequest request = new AuthenticationRequest("test@email.com", "Test12345", "Test User1");
        String jsonRequest = mapper.writeValueAsString(request);
        MvcResult result = mockMvc
                .perform(post("/api/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        UserEntity newUser = userRepository.findByUsername("test@email.com");
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("Test User1", newUser.getName());
        System.out.println(userRepository.findAll());
        // userRepository.delete(newUser);
        // clean up everytime testuser is added
    }

    // @Test
    // void testRegister() throws Exception {
    // AuthenticationRequest request = new AuthenticationRequest("test1@email.com",
    // "Test12345", "Test User1");
    // String jsonRequest = mapper.writeValueAsString(request);
    // System.out.println("---------------------" + homeController +
    // "---------------------");
    // mockMvc.perform(post("/api/register").content(jsonRequest).contentType(MediaType.APPLICATION_JSON));
    // // assertEquals(200, result.getResponse().getStatus());
    // }

    // @Test
    // void testRegister() throws Exception {
    // UserEntity newUser = new UserEntity("test1@email.com", "Test User1",
    // bcryptEncoder.encode("Test1234"),
    // "ROLE_USER", true);
    // when(userRepository.save(newUser)).thenReturn(newUser);
    // String url = "/api/register";
    // AuthenticationRequest request = new AuthenticationRequest("test1@email.com",
    // "Test12345", "Test User1");
    // String jsonRequest = mapper.writeValueAsString(request);
    // MvcResult result =
    // mockMvc.perform(post(url).content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
    // .andReturn();
    // assertEquals(200, result.getResponse().getStatus());
    // }

    @Test
    void testAuthenticateUser() throws Exception {

        AuthenticationRequest request = new AuthenticationRequest();

        // userRepository.deleteAll();
        request.setUsername("tien@email.com");
        request.setPassword("Tien12345");
        String jsonRequest = mapper.writeValueAsString(request);
        MvcResult result = mockMvc
                .perform(post("/api/authenticate").content(jsonRequest).contentType(MediaType.APPLICATION_JSON))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }
}
