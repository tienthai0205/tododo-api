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
public class Note {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private User createdBy;
    private String title;
    private String description; 
    private Group group;
    
    @ManyToMany
    private Set<Tag> tags;
    @ManyToMany
    private Set<User> sharedWith;  

    public Note(User createdBy, String title, String description, Group group, Set<Tag> tags, Set<User> sharedWith) {
        this.createdBy = createdBy;
        this.title = title;
        this.description = description;
        this.group = group;
        this.tags = tags;
        this.sharedWith = sharedWith;
    }

    public Note() {
    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return this.createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
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

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public Set<User> getSharedWith() {
        return this.sharedWith;
    }

    public void setSharedWith(Set<User> sharedWith) {
        this.sharedWith = sharedWith;
    }

}
