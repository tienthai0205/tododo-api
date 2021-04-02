package com.tododo.api;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tododo.api.models.AuthenticationRequest;
import com.tododo.api.models.Note;
import com.tododo.api.models.Tag;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.NoteRepository;
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
public class ApiNoteTests {
    private MockMvc mockMvc;

    private String accessToken;

    @Autowired
    private ObjectMapper mapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private NoteRepository noteRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TagRepository tagRepository;

    @BeforeEach
    public void setup() throws Exception {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
        getAccessToken();
    }

    @Test
    void getNotesForUser() throws Exception {
        noteRepository.deleteAll();
        String url = "/api/notes";
        MvcResult forbiddenResult = mockMvc.perform(get(url)).andReturn();
        assertEquals(401, forbiddenResult.getResponse().getStatus());
        MvcResult result = mockMvc.perform(get(url).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
        assertEquals("[]", result.getResponse().getContentAsString());
    }

    @Test
    void getNote() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Note newNote = new Note("Note1", "Test note1");
        newNote.setUser(currentUser);
        int id = noteRepository.save(newNote).getId();

        String url = "/api/notes/{id}";
        MvcResult notfoundResult = mockMvc.perform(get(url, 30).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(404, notfoundResult.getResponse().getStatus());
        assertEquals("Note with id 30 not found", notfoundResult.getResponse().getContentAsString());

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Note maxNote = new Note("Note10", "Test note10");
        maxNote.setUser(anotherUser);
        int maxNoteId = noteRepository.save(maxNote).getId();
        MvcResult forbiddenResult = mockMvc
                .perform(get(url, maxNoteId).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(403, forbiddenResult.getResponse().getStatus());

        MvcResult result = mockMvc.perform(get(url, id).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addNote() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Note newNote = new Note("Note1", "Test note1");
        newNote.setUser(currentUser);
        String jsonRequest = mapper.writeValueAsString(newNote);
        String url = "/api/notes";
        MvcResult result = mockMvc.perform(post(url).content(jsonRequest).contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(201, result.getResponse().getStatus());
    }

    @Test
    void deleteNote() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Note newNote = new Note("Note1", "Test note1");
        newNote.setUser(currentUser);
        int id = noteRepository.save(newNote).getId();

        String url = "/api/notes/{id}";
        MvcResult notfoundResult = mockMvc.perform(delete(url, 40).header("Authorization", "Bearer " + accessToken))
                .andReturn();

        assertEquals(404, notfoundResult.getResponse().getStatus());
        assertEquals("Note with id 40 not found", notfoundResult.getResponse().getContentAsString());

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Note maxNote = new Note("Note10", "Test note10");
        maxNote.setUser(anotherUser);
        int maxNoteId = noteRepository.save(maxNote).getId();

        MvcResult forbiddenResult = mockMvc
                .perform(delete(url, maxNoteId).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(403, forbiddenResult.getResponse().getStatus());

        MvcResult result = mockMvc.perform(delete(url, id).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals("Your request has been successfully handled!", result.getResponse().getContentAsString());
    }

    @Test
    void removeTagFromNote() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Note newNote = new Note("Note1", "Test note1");
        newNote.setUser(currentUser);
        int id = noteRepository.save(newNote).getId();

        Tag newTag = new Tag("Tag4", "Test tag4 for note1");
        newTag.setUser(currentUser);
        int tagId = tagRepository.save(newTag).getId();

        newNote.addTag(newTag);

        String url = "/api/notes/{id}/tag/{tagId}";
        MvcResult result = mockMvc.perform(delete(url, id, tagId).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void addTagToNote() throws Exception {
        UserEntity currentUser = userRepository.findByUsername("tien@email.com");
        Note newNote = new Note("Note1", "Test note1");
        newNote.setUser(currentUser);
        int id = noteRepository.save(newNote).getId();

        Tag newTag = new Tag("Tag5", "Test tag5 for note1");
        newTag.setUser(currentUser);
        int tagId = tagRepository.save(newTag).getId();

        String url = "/api/notes/{id}/tag/{tagId}";
        MvcResult result = mockMvc.perform(put(url, id, tagId).header("Authorization", "Bearer " + accessToken))
                .andReturn();
        assertEquals(200, result.getResponse().getStatus());
    }

    @Test
    void getTags() throws Exception {
        String url = "/api/notes/{id}/tags";

        UserEntity anotherUser = userRepository.findByUsername("max@email.com");
        Note maxNote = new Note("Note10", "Test note10");
        maxNote.setUser(anotherUser);
        int maxNoteId = noteRepository.save(maxNote).getId();

        MvcResult forbiddenResult = mockMvc
                .perform(get(url, maxNoteId).header("Authorization", "Bearer " + accessToken)).andReturn();
        assertEquals(403, forbiddenResult.getResponse().getStatus());

        MvcResult result = mockMvc.perform(get(url, 1).header("Authorization", "Bearer " + accessToken)).andReturn();
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
