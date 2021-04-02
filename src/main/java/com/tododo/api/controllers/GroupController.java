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
    private final UserRepository userRepository;

    public GroupController(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
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
        Group memberGroup = new Group(newGroup.getName(), newGroup.getDescription());
        memberGroup.setType(GroupTypeEnum.GROUP_MEMBER);
        newGroup.setType(GroupTypeEnum.GROUP_ADMIN);
        groupRepository.save(newGroup);
        groupRepository.save(memberGroup);

        newGroup.addMember(currentUser(principal));
        return new ResponseEntity<>(groupRepository.save(newGroup), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/members/{userId}")
    public ResponseEntity<?> addMemberToGroup(Principal principal, @PathVariable int id, @PathVariable int userId) {
        Group group = groupRepository.findById(id);
        if (group == null) {
            return new ResponseEntity<>("Group with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        UserEntity newMember = userRepository.findById(userId);
        if (newMember == null) {
            return new ResponseEntity<>("User with id " + userId + " not found", HttpStatus.NOT_FOUND);
        }
        group.addMember(newMember);
        return ResponseEntity.ok(groupRepository.save(group));
    }

    @DeleteMapping("/{id}/members/{userId}")
    public ResponseEntity<?> removeMemberFromGroup(Principal principal, @PathVariable int id,
            @PathVariable int userId) {
        Group group = groupRepository.findById(id);
        if (group == null) {
            return new ResponseEntity<>("Group with id " + id + " not found", HttpStatus.NOT_FOUND);
        }
        UserEntity member = userRepository.findById(userId);
        if (member == null) {
            return new ResponseEntity<>("User with id " + userId + " not found", HttpStatus.NOT_FOUND);
        }
        group.removeMember(member);
        return ResponseEntity.ok("Your request has been successfully handled!");
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
