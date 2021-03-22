package com.tododo.api.repositories;

import com.tododo.api.models.UserGroup;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends CrudRepository<UserGroup, Integer> {
    @Query(value = "select * from user_group where id=?1", nativeQuery = true)
    UserGroup findById(int id);
}
