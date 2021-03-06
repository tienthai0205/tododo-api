package com.tododo.api.controllers;

import java.security.Principal;

import com.tododo.api.models.*;
import com.tododo.api.repositories.UserRepository;
import com.tododo.api.services.JwtUtil;
import com.tododo.api.services.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api")
public class HomeController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/admin/hello")
    public ResponseEntity<?> hello() {
        return ResponseEntity.ok("Hello user!");
    }

    @GetMapping("/admin/users")
    public ResponseEntity<?> getUserList() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok(new AuthenticationResponse(token));
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody AuthenticationRequest user) {
        UserEntity newUser = userDetailsService.save(user);
        if (newUser == null) {
            return new ResponseEntity<>("User with that username already exists!", HttpStatus.BAD_REQUEST);
        }

        return ResponseEntity.ok(newUser);
    }

    @GetMapping("/account")
    public ResponseEntity<?> getAccountInfo(Principal principal) {
        UserEntity currentUser = userRepository.findByUsername(principal.getName());

        return ResponseEntity.ok(currentUser);
    }

    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
