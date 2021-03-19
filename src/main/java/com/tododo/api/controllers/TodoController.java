package com.tododo.api.controllers;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

import com.tododo.api.models.Todo;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.TodoRepository;
import com.tododo.api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    @Autowired
    private TodoRepository todoRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<?> todoList(Principal principal) {

        List<Todo> todos = todoRepository.findByUserId(getCurrentUserId(principal));
        return ResponseEntity.ok(todos);
    }

    private int getCurrentUserId(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        int userId = currentUser.getId();
        return userId;
    }
}
