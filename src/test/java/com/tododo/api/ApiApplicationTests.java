package com.tododo.api;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@SpringBootTest
class ApiApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext context;

	@BeforeEach
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
				.build();
	}

	@Test
	void testHello() throws Exception {
		MvcResult failureResult = mockMvc.perform(get("/api/admin/hello").contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(401, failureResult.getResponse().getStatus());
	}

	@WithMockUser(roles = "ADMIN")
	@Test
	void testHelloWithAdmin() throws Exception {
		MvcResult failureResult = mockMvc.perform(get("/api/admin/hello").contentType(MediaType.APPLICATION_JSON))
				.andReturn();
		assertEquals(200, failureResult.getResponse().getStatus());
	}

}
