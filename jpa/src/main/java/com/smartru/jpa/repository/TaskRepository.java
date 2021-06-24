package com.smartru.jpa.repository;

import com.smartru.common.entity.Task;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task,Long> {

    @Query("SELECT t FROM Task t WHERE t.user.login = :login AND t.status = 'ACTIVE'")
    List<Task> findByUser(@Param("login") String login);

    @Query("SELECT t FROM Task t LEFT JOIN FETCH t.user WHERE t.id = :id")
    Optional<Task> getFullTaskById(@Param("id") long id);

    @Query(nativeQuery = true,
            value = "SELECT * FROM tasks WHERE num = :num AND result IS NOT NULL LIMIT 1")
    Optional<Task> findPerformedTaskByNum(@Param("num") String num);

    @Modifying
    @Transactional
    @Query("UPDATE Task t SET t.result=:result WHERE t.id=:id")
    void setResult(@Param("result")TaskResult result,
                   @Param("id")long taskId);
}
