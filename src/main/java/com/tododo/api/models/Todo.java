package com.tododo.api.models;

import java.sql.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "todo")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column
    private String title;
    @Column
    private String description;
    @Column
    private float percentage;
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

    @ManyToMany
    @JoinTable(name = "todoTag", joinColumns = @JoinColumn(name = "tag_id"), inverseJoinColumns = @JoinColumn(name = "todo_id"))
    private Set<Tag> tags;

    public Todo() {
    }

    public Todo(int id, UserEntity user, String title, String description, float percentage, long duration,
            Date dueDate, Set<Tag> tags) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.percentage = percentage;
        this.duration = duration;
        this.dueDate = dueDate;
        this.tags = tags;
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

}
