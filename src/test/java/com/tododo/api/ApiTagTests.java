package com.tododo.api;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.AuthenticationRequest;
import com.tododo.api.models.Tag;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.TagRepository;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@ActiveProfiles("test")
public class ApiTagTests {
	private MockMvc mockMvc;

	private String accessToken;

	@Autowired
	private ObjectMapper mapper;

	@Autowired
	private WebApplicationContext context;

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
	void getAllTags() throws Exception {
		String url = "/api/tags/all";
		MvcResult forbiddenResult = mockMvc.perform(get(url)).andReturn();
		assertEquals(401, forbiddenResult.getResponse().getStatus());
		MvcResult result = mockMvc.perform(get(url).header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void getAllNoteTags() throws Exception {
		String url = "/api/tags";
		MvcResult forbiddenResult = mockMvc.perform(get(url).param("type", "note")).andReturn();
		assertEquals(401, forbiddenResult.getResponse().getStatus());

		MvcResult badResult = mockMvc.perform(get(url).header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(400, badResult.getResponse().getStatus());

		MvcResult result = mockMvc
				.perform(get(url).param("type", "note").header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(200, result.getResponse().getStatus());

		MvcResult notsupportResult = mockMvc
				.perform(get(url).param("type", "fail").header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(400, notsupportResult.getResponse().getStatus());
		assertEquals("Please enter a supported endpoint", notsupportResult.getResponse().getContentAsString());
	}

	@Test
	void getAllTodoTags() throws Exception {
		String url = "/api/tags";
		MvcResult forbiddenResult = mockMvc.perform(get(url).param("type", "todo")).andReturn();
		assertEquals(401, forbiddenResult.getResponse().getStatus());

		MvcResult badResult = mockMvc.perform(get(url).header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(400, badResult.getResponse().getStatus());

		MvcResult result = mockMvc
				.perform(get(url).param("type", "todo").header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void addTag() throws Exception {
		Tag newTag = new Tag("Tag2", "Test Tag1 description");
		String jsonRequest = mapper.writeValueAsString(newTag);
		String url = "/api/tags";
		MvcResult result = mockMvc.perform(post(url).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(201, result.getResponse().getStatus());
	}

	@Test
	void editTag() throws Exception {
		Tag updatedtag = new Tag("Tag2", "Test Tag1 description");
		String jsonRequest = mapper.writeValueAsString(updatedtag);
		String url = "/api/tags/{id}";
		MvcResult notfoundResult = mockMvc.perform(put(url, 100).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(404, notfoundResult.getResponse().getStatus());

		UserEntity anotherUser = userRepository.findByUsername("max@email.com");
		Tag newTag = new Tag("Tag6", "Test tag6 for user max");
		newTag.setUser(anotherUser);
		int maxTagId = tagRepository.save(newTag).getId();

		MvcResult forbiddenResult = mockMvc.perform(put(url, maxTagId).content(jsonRequest)
				.contentType(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(403, forbiddenResult.getResponse().getStatus());
		Tag validTag = new Tag("Tag7", "Test tag7 for current user");
		validTag.setUser(userRepository.findByUsername("tien@email.com"));
		int id = tagRepository.save(validTag).getId();
		MvcResult result = mockMvc.perform(put(url, id).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
				.header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(200, result.getResponse().getStatus());
	}

	@Test
	void removeTag() throws Exception {

		String url = "/api/tags/{id}";
		MvcResult notfoundResult = mockMvc.perform(delete(url, 100).header("Authorization", "Bearer " + accessToken))
				.andReturn();
		assertEquals(404, notfoundResult.getResponse().getStatus());

		UserEntity anotherUser = userRepository.findByUsername("max@email.com");
		Tag newTag = new Tag("Tag8", "Test tag8 for user max");
		newTag.setUser(anotherUser);
		int maxTagId = tagRepository.save(newTag).getId();

		MvcResult forbiddenResult = mockMvc
				.perform(delete(url, maxTagId).header("Authorization", "Bearer " + accessToken)).andReturn();
		assertEquals(403, forbiddenResult.getResponse().getStatus());

		Tag validTag = new Tag("Tag9", "Test tag9 for current user");
		validTag.setUser(userRepository.findByUsername("tien@email.com"));
		int id = tagRepository.save(validTag).getId();

		MvcResult result = mockMvc.perform(delete(url, id).header("Authorization", "Bearer " + accessToken))
				.andReturn();
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
