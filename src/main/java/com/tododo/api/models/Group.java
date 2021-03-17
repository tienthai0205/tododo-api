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
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name; 
    
    @ManyToMany
    private Set<User> members;
    @ManyToMany
    private Set<Note> notes;  
    @ManyToMany
    private Set<Todo> items; 

    public Group(String name, Set<User> members, Set<Note> notes, Set<Todo> items) {
        this.name = name;
        this.members = members;
        this.notes = notes;
        this.items = items;
    }

    public Group() {
    }
    
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<User> getMembers() {
        return this.members;
    }

    public void setMembers(Set<User> members) {
        this.members = members;
    }

    public Set<Note> getNotes() {
        return this.notes;
    }

    public void setNotes(Set<Note> notes) {
        this.notes = notes;
    }

    public Set<Todo> getItems() {
        return this.items;
    }

    public void setItems(Set<Todo> items) {
        this.items = items;
    }
    
}
