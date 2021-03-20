package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.Note;
import com.tododo.api.models.Tag;
import com.tododo.api.repositories.NoteRepository;
import com.tododo.api.repositories.TagRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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

    @GetMapping("")
    public ResponseEntity<?> getTagList(Principal principal) {

        Iterable<Tag> tags = tagRepository.findAll();

        return ResponseEntity.ok(tags);
    }

}
