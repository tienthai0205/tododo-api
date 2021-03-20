package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;

import com.tododo.api.models.Todo;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.TodoRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoRepository todoRepository;

    private final UserRepository userRepository;

    public TodoController(TodoRepository todoRepository, UserRepository userRepository) {
        this.todoRepository = todoRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public ResponseEntity<?> getTodoList(Principal principal) {

        List<Todo> todos = todoRepository.findByUserId(currentUser(principal).getId());
        return ResponseEntity.ok(todos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTodoItem(Principal principal, @PathVariable int id) {
        Todo todo = todoRepository.findById(id);

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

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }

}
