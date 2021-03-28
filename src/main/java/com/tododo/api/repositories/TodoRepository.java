package com.tododo.api.repositories;

import java.util.List;

import com.tododo.api.models.Todo;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends CrudRepository<Todo, Integer> {
    Todo findById(int id);

    List<Todo> findByUserId(int id);
}
