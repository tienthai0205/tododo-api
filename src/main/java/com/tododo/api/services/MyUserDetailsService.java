package com.tododo.api.services;

import java.util.Optional;

import com.tododo.api.models.AuthenticationRequest;
import com.tododo.api.models.MyUserDetails;
import com.tododo.api.models.UserEntity;
import com.tododo.api.repositories.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        return user.map(MyUserDetails::new).get();
        // return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }

    public UserEntity save(AuthenticationRequest user) {
        Optional<UserEntity> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser != null) {
            return null;
        }
        UserEntity newUser = new UserEntity();
        newUser.setUsername(user.getUsername());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        newUser.setActive(true);
        newUser.setRole("ROLE_USER");
        return userRepository.save(newUser);
    }

}