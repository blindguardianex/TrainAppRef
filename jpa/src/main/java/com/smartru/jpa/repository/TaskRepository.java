package com.smartru.jpa.repository;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t WHERE t.user.login = :login")
    List<Task> findByUser(@Param("login") String login);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE t.id = :id")
    Optional<Task> getFullTaskById(@Param("id") long id);
}
