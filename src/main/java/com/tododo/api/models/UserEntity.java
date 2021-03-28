package com.tododo.api.models;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "user")
public class UserEntity extends BaseModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String username;
    @Column
    private String password;
    @Column
    private boolean active;
    @Column
    private String role;
    @Column
    private String name;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonManagedReference()
    private Set<Todo> todoItems;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonManagedReference()
    private Set<Note> notes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonFormat(shape = JsonFormat.Shape.ARRAY)
    @JsonManagedReference()
    private Set<Tag> tags;

    public int getId() {
        return this.id;
    }

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "usergroup-user")
    private Set<UserGroup> userGroups;

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public Set<Note> getNotes() {
        return notes;
    }

    public Set<Todo> getTodoItems() {
        return todoItems;
    }

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }
}
