package com.tododo.api.repositories;

import java.util.List;

import com.tododo.api.models.Note;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends CrudRepository<Note, Integer> {
    Note findById(int id);

    List<Note> findByUserId(int id);
}
