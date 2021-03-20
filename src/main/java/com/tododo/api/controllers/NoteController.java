package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.Note;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.NoteRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @RequestMapping(value = "", consumes = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<?> addNewNote(@RequestBody Note newNote, Principal principal) {
        newNote.setUser(currentUser(principal));
        noteRepository.save(newNote);
        return new ResponseEntity<>(newNote, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getNote(Principal principal, @PathVariable int id) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return new ResponseEntity<>("Note with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (note.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(note);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> updateTodoItem(Principal principal, @PathVariable int id) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return new ResponseEntity<>("Todo item with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (note.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }
        noteRepository.delete(note);

        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }
}
