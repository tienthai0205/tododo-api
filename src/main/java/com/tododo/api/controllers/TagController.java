package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.Tag;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.TagRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/tags")
public class TagController {
    private final TagRepository tagRepository;

    private final UserRepository userRepository;

    public TagController(TagRepository tagRepository, UserRepository userRepository) {
        this.tagRepository = tagRepository;
        this.userRepository = userRepository;
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

    @PutMapping("/{id}")
    public ResponseEntity<?> editTag(Principal principal, @PathVariable int id, @RequestBody Tag updatetag) {
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            return new ResponseEntity<>("Tag with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        if (tag.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }
        tag.setTitle(updatetag.getTitle());
        tag.setDescription(updatetag.getDescription());

        return ResponseEntity.ok("Your request has been successfully handled!");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> removeTag(Principal principal, @PathVariable int id) {
        Tag tag = tagRepository.findById(id);
        if (tag == null) {
            return new ResponseEntity<>("Tag with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        if (tag.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }

        // tagRepository.deleteTagAssociation(id);
        tagRepository.delete(tag);

        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }

}
