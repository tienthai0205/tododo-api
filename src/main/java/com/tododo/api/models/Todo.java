package com.tododo.api.models;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
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
    // private Set<UserEntity> users;
    // @ManyToMany
    // private Set<Tag> tags;

}
