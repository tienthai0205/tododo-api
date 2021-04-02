package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.Tag;
import com.tododo.api.models.Todo;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;
    private final TagRepository tagRepository;

    public TodoController(TodoRepository todoRepository, UserRepository userRepository, TagRepository tagRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
        this.tagRepository = tagRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getTodoList(Principal principal) {

        List<Todo> todos = todoRepository.findByUserId(currentUser(principal).getId());

        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoItem(Principal principal, @PathVariable int id) {
        Todo todo = todoRepository.findById(id);
        if (todo == null) {
            return new ResponseEntity<>("Todo item with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (todo.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }

        return ResponseEntity.ok(todo);

    }

    @RequestMapping(value = "", consumes = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<?> addNewTodo(@RequestBody Todo newTodo, Principal principal) throws Exception {
        newTodo.setUser(currentUser(principal));
        todoRepository.save(newTodo);
        return new ResponseEntity<>(newTodo, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTodoItem(Principal principal, @PathVariable int id, @RequestBody Todo updateTodo) {
        Todo todo = todoRepository.findById(id);

        if (todo == null) {
            return new ResponseEntity<>("Todo item with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (todo.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }

        // TODO: Find a more generic or smart way to handle this
        todo.setTitle(updateTodo.getTitle());
        todo.setDescription(updateTodo.getDescription());
        todo.setDuration(updateTodo.getDuration());
        todo.setPercentage(updateTodo.getPercentage());
        return ResponseEntity.ok(todo);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTodoItem(Principal principal, @PathVariable int id) {
        Todo todo = todoRepository.findById(id);
        if (todo == null) {
            return new ResponseEntity<>("Todo item with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        if (todo.getUser().getId() != currentUser(principal).getId()) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }
        todoRepository.delete(todo);

        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    @GetMapping("/{id}/tags")
    public ResponseEntity<?> getTags(Principal principal, @PathVariable int id) {
        Todo todo = todoRepository.findById(id);
        int userId = currentUser(principal).getId();
        if (todo.getUser().getId() != userId) {
            return new ResponseEntity<>("You are not authorized to retrieved this item", HttpStatus.FORBIDDEN);
        }
        List<Tag> tags = tagRepository.findByTodoId(id, userId);

        return ResponseEntity.ok(tags);
    }

    @PutMapping("/{id}/tag/{tagId}")
    public ResponseEntity<?> addTagToTodo(Principal principal, @PathVariable int id, @PathVariable int tagId) {
        Todo todo = todoRepository.findById(id);
        Tag tag = tagRepository.findById(tagId);
        todo.addTag(tag);

        return ResponseEntity.ok(todoRepository.save(todo));

    }

    @DeleteMapping("/{id}/tag/{tagId}")
    public ResponseEntity<?> removeTagFromTodo(Principal principal, @PathVariable int id, @PathVariable int tagId) {
        Todo todo = todoRepository.findById(id);
        Tag tag = tagRepository.findById(tagId);
        todo.removeTag(tag);

        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }

}
