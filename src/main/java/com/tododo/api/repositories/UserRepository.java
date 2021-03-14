package com.tododo.api.repositories;

import java.util.Optional;

import com.tododo.api.models.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
