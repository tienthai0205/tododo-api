package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.Note;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.NoteRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    public NoteController(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getNoteList(Principal principal) {

        List<Note> notes = noteRepository.findByUserId(currentUser(principal).getId());

        return ResponseEntity.ok(notes);
    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }
}
