package com.tododo.api.repositories;

import java.util.List;

import com.tododo.api.models.Tag;

import org.springframework.data.repository.CrudRepository;

public interface TagRepository extends CrudRepository<Tag, Integer> {

    Tag findById(int id);

    // @Query("select t from tag t join note_tag nt on nt.tag_id=t.id join note n on
    // nt.note_id=n.id")
    // List<Tag> findTagsByNoteId(int id);
}
