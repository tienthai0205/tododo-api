package com.tododo.api.controllers;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import com.tododo.api.models.Note;
import com.tododo.api.models.Tag;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.NoteRepository;
import com.tododo.api.repositories.TagRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepository;

    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    public TagController(TagRepository tagRepository, UserRepository userRepository, NoteRepository noteRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getTagList(Principal principal) {
        int userId = currentUser(principal).getId();

        List<Tag> allTags = tagRepository.findByUserId(userId);
        return ResponseEntity.ok(allTags);
    }

    @GetMapping("")
    public ResponseEntity<?> getTagsByType(Principal principal, @RequestParam String type) {
        int userId = currentUser(principal).getId();
        if (type.equals("note")) {
            List<Tag> tags = tagRepository.findNoteTagsByUser(userId);
            return ResponseEntity.ok(tags);
        }
        if (type.equals("todo")) {
            List<Tag> tags = tagRepository.findTodoTagsByUser(userId);
            return ResponseEntity.ok(tags);
        }
        return new ResponseEntity<>("Please enter a supported endpoint", HttpStatus.BAD_REQUEST);
    }

    @PostMapping("")
    public ResponseEntity<?> addTag(@RequestBody Tag newTag, Principal principal) {
        newTag.setUser(currentUser(principal));
        tagRepository.save(newTag);
        return new ResponseEntity<>(newTag, HttpStatus.CREATED);

    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }

}
