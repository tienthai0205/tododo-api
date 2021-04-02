package com.tododo.api.models;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

@Entity
@Table(name = "`group`", uniqueConstraints = { @UniqueConstraint(columnNames = { "name", "type" }) })
public class Group extends BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column
    private String name;
    @Column
    private String description;
    @Column
    private GroupTypeEnum type;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "groupUser", joinColumns = @JoinColumn(name = "group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<UserEntity> users = new HashSet<>();

    public Group() {
    }

    public Group(String name, String description) {
        this.name = name;
        this.description = description;
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

    public Set<UserEntity> getUsers() {
        return users;
    }

    public GroupTypeEnum getType() {
        return type;
    }

    public void setType(GroupTypeEnum type) {
        this.type = type;
    }

    public void addMember(UserEntity member) {
        this.users.add(member);
    }

    public void removeMember(UserEntity member) {
        this.users.remove(member);
    }
}
