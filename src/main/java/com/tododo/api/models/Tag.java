package com.tododo.api.models;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true) // you can't have tag "work" and "work" for the same user
    private String title;
    private String description;
    
    @ManyToMany
    private Set<Todo> items;
    @ManyToMany
    private Set<Note> notes; 
    

    public Tag(String title, String description, Set<Todo> items, Set<Note> notes) {
        this.title = title;
        this.description = description;
        this.items = items;
        this.notes = notes;
    }

    public Tag() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public Set<Todo> getItems() {
        return this.items;
    }

    public void setItems(Set<Todo> items) {
        this.items = items;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

}
