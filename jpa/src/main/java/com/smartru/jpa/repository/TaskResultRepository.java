package com.smartru.jpa.repository;

import com.smartru.common.entity.BaseEntity;
import com.smartru.common.entity.TaskResult;
import com.smartru.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TaskResultRepository extends JpaRepository<TaskResult,Long> {

    @Modifying
    @Query("UPDATE TaskResult SET status = :status WHERE id = :id")
    void updateStatus(@Param("id")long id, @Param("status")BaseEntity.Status status);
}
