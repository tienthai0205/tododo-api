package com.tododo.api.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "tag")
public class Tag extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String title;
    private String description;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    private Set<Todo> todoItems;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Note> notes;

    // @Query(value = "select * user_id from note where note.id=")
    // private UserEntity user;

    public Tag() {
    }

    public Tag(int id, String title, String description, Set<Todo> todoItems, Set<Note> notes) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.todoItems = todoItems;
        this.notes = notes;
    }

    public int getId() {
        return this.id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<Todo> getTodoItems() {
        return this.todoItems;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void addNote(Note note) {
        this.notes.add(note);
    }

    public void addTodo(Todo todo) {
        this.todoItems.add(todo);
    }

}
