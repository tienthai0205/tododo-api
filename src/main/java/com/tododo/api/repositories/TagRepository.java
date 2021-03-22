package com.tododo.api.repositories;

import java.util.List;

import com.tododo.api.models.Tag;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends CrudRepository<Tag, Integer> {

    Tag findById(int id);

    @Query(value = "select * from tag t join note_tag nt on nt.tag_id=t.id join note n on nt.note_id=n.id where n.id=?1 and n.user_id=?2", nativeQuery = true)
    List<Tag> findByNoteId(int id, int userId);

    @Query(value = "select * from tag t join todo_tag tt on tt.tag_id=t.id join todo tod on tt.todo_id=tod.id where tod.id=?1 and tod.user_id=?2", nativeQuery = true)
    List<Tag> findByTodoId(int id, int userId);

    @Query(value = "select * from tag t join note_tag nt on nt.tag_id=t.id join note n on nt.note_id=n.id where n.user_id=?1", nativeQuery = true)
    List<Tag> findNoteTagsByUser(int id);

    @Query(value = "select * from tag t join todo_tag tt on tt.tag_id=t.id join todo tod on tt.todo_id=tod.id where tod.user_id=?1", nativeQuery = true)
    List<Tag> findTodoTagsByUser(int id);

    @Query(value = "delete tt, nt from todo_tag tt join note_tag nt on tt.tag_id=nt.tag_id where tt.tag_id=?1", nativeQuery = true)
    void deleteTagAssociation(int id);

    List<Tag> findByUserId(int id);

}
