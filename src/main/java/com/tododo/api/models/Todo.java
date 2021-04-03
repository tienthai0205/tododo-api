package com.tododo.api.models;

import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "todo")
public class Todo extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false, referencedColumnName = "id")
    @JsonBackReference
    private UserEntity user;

    @Column
    private String title;
    @Column
    private String description;
    @Column
    private float percentage = 0.0f;
    @Column
    private long duration;
    @Column
    private Date dueDate;

    // private Group group;

    // @ManyToMany(fetch = FetchType.LAZY)
    // @JoinTable(name = "user", joinColumns = @JoinColumn(name = "user_id"),
    // inverseJoinColumns = @JoinColumn(name = "todo_id"))
    // @Column(name = "user")
    // private Set<UserEntity> shareWith;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "todoTag", joinColumns = @JoinColumn(name = "todo_id"), inverseJoinColumns = @JoinColumn(name = "tag_id"))
    private Set<Tag> tags = new HashSet<>();

    public Todo() {
    }

    public Todo(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public Todo(String title, String description, long duration, float percentage) {
        this.title = title;
        this.description = description;
        this.duration = duration;
        this.percentage = percentage;
    }

    public int getId() {
        return this.id;
    }

    public UserEntity getUser() {
        return this.user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
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

    public float getPercentage() {
        return this.percentage;
    }

    public void setPercentage(float percentage) {
        this.percentage = percentage;
    }

    public long getDuration() {
        return this.duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public Date getDueDate() {
        return this.dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Set<Tag> getTags() {
        return this.tags;
    }

    public void addTag(Tag tag) {
        this.tags.add(tag);
    }

    public void removeTag(Tag tag) {
        tags.remove(tag);
    }

}
