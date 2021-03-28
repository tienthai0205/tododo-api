package com.tododo.api.models;

import java.util.Set;

import javax.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Set<Todo> todoItems;

    @JsonIgnore
    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private Set<Note> notes;

    // @Query(value = "select * user_id from note where note.id=")
    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private UserEntity user;

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

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

}
