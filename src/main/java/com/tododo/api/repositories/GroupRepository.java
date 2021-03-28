package com.tododo.api.repositories;

import com.tododo.api.models.Group;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends CrudRepository<Group, Integer> {
    Group findById(int id);
}
