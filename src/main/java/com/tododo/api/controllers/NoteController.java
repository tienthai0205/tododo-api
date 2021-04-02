package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.*;
import com.tododo.api.repositories.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/notes")
public class NoteController {

    private final NoteRepository noteRepository;

    private final UserRepository userRepository;

    private final TagRepository tagRepository;

    public NoteController(NoteRepository noteRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
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
    public ResponseEntity<?> deleteNote(Principal principal, @PathVariable int id) {
        Note note = noteRepository.findById(id);
        if (note == null) {
            return new ResponseEntity<>("Note with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (note.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }
        noteRepository.delete(note);

        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    @PutMapping("/{noteId}/tag/{tagId}")
    public ResponseEntity<?> addTagToNote(Principal principal, @PathVariable int noteId, @PathVariable int tagId) {
        Note note = noteRepository.findById(noteId);
        Tag tag = tagRepository.findById(tagId);
        note.addTag(tag);

        return ResponseEntity.ok(noteRepository.save(note));

    }

    @DeleteMapping("/{noteId}/tag/{tagId}")
    public ResponseEntity<?> removeTagFromNote(Principal principal, @PathVariable int noteId, @PathVariable int tagId) {
        Note note = noteRepository.findById(noteId);
        Tag tag = tagRepository.findById(tagId);
        note.removeTag(tag);

        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<?> getTags(Principal principal, @PathVariable int id) {
        Note note = noteRepository.findById(id);
        int userId = currentUser(principal).getId();
        if (note.getUser().getId() != userId) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }
        List<Tag> tags = tagRepository.findByNoteId(id, userId);

        return ResponseEntity.ok(tags);
    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }
}
