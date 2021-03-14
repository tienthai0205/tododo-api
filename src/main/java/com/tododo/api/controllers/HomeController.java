package com.tododo.api.controllers;

import com.tododo.api.models.MyUserDetails;
import com.tododo.api.models.User;
import com.tododo.api.services.JwtUtil;
import com.tododo.api.services.MyUserDetailsService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    BCryptPasswordEncoder b = new BCryptPasswordEncoder();

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    PasswordEncoder bcryptEncoder;

    @Autowired
    private JwtUtil jwtTokenUtil;

    @GetMapping("/")
    public String home() {
        return ("<h1>Welcome</h1>");
    }

    @GetMapping("/user")
    public String user() {
        return ("<h1>Welcome User</h1>");
    }

    @GetMapping("/admin")
    public String admin() {
        return ("<h1>Welcome Admin</h1>");
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody User user)
            throws Exception, UsernameNotFoundException {

        // try {
        // authenticationManager
        // .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),
        // user.getPassword()));
        // } catch (BadCredentialsException e) {
        // throw new Exception("Incorrect username or password");
        // }
        String username = user.getUsername();
        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!b.matches(user.getPassword(), userDetails.getPassword())) {
            return new ResponseEntity<>("Wrong password! ", HttpStatus.BAD_REQUEST);
        }

        final String jwt = jwtTokenUtil.generateToken(userDetails);

        return ResponseEntity.ok("accessToken: " + jwt);
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ResponseEntity<?> saveUser(@RequestBody User user) {

        UserDetails existingUser = userDetailsService.loadUserByUsername(user.getUsername());
        if (existingUser != null) {

            return new ResponseEntity<>("User with that username already exist!", HttpStatus.BAD_REQUEST);
        }
        User newUser = new User();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return ResponseEntity.ok(newUser);
    }

}
