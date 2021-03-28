package com.tododo.api.controllers;

import java.security.Principal;

import com.tododo.api.models.*;
import com.tododo.api.repositories.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/groups")
public class GroupController {
    private final GroupRepository groupRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;

    public GroupController(GroupRepository groupRepository, UserGroupRepository userGroupRepository,
            UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userGroupRepository = userGroupRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllGroups(Principal principal) {

        Iterable<Group> groups = groupRepository.findAll();

        return ResponseEntity.ok(groups);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getGroup(Principal principal, @PathVariable int id) {
        Group group = groupRepository.findById(id);

        if (group == null) {
            return new ResponseEntity<>("Group with id " + id + " not found", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(group);
    }

    @RequestMapping(value = "", consumes = { "application/json" }, method = RequestMethod.POST)
    public ResponseEntity<?> addGroup(@RequestBody Group newGroup, Principal principal) {
        groupRepository.save(newGroup);
        userGroupRepository.save(new UserGroup(currentUser(principal), newGroup, "ADMIN"));
        return new ResponseEntity<>(newGroup, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/members/{userId}")
    public ResponseEntity<?> addMemberToGroup(Principal principal, @PathVariable int id, @PathVariable int userId) {
        Group group = groupRepository.findById(id);
        if (group == null) {
            return new ResponseEntity<>("Group with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        UserEntity newMember = userRepository.findById(userId);
        if (newMember == null) {
            return new ResponseEntity<>("User with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        UserGroup userGroup = new UserGroup(newMember, group, "MEMBER");
        return ResponseEntity.ok(userGroupRepository.save(userGroup));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGroup(Principal principal, @PathVariable int id) {
        Group group = groupRepository.findById(id);
        if (group == null) {
            return new ResponseEntity<>("Group with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        groupRepository.delete(group);
        return ResponseEntity.ok("Your request has been successfully handled!");
    }

    private UserEntity currentUser(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());
        return currentUser;
    }
}
