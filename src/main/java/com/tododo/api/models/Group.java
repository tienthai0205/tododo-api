package com.tododo.api.models;

import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "`group`")
public class Group extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(unique = true)
    private String name;
    @Column
    private String description;

    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JsonManagedReference(value = "usergroup-group")
    private Set<UserGroup> userGroups;

    public Group() {
    }

    public Group(int id, String name, String description, Set<UserGroup> userGroups) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.userGroups = userGroups;
    }

    public int getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<UserGroup> getUserGroups() {
        return userGroups;
    }

}
