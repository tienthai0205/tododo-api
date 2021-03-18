package com.tododo.api.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import org.springframework.data.jpa.repository.Query;

@Entity
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String title;
    private String description;

    @ManyToMany(mappedBy = "tags")
    private Set<Todo> todoItems;

    @ManyToMany(mappedBy = "tags")
    private Set<Note> notes;

    // @Query(value = "select * user_id from note where note.id=")
    // private UserEntity user;

}
