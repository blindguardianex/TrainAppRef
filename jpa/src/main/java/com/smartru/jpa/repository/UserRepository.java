package com.smartru.jpa.repository;

import com.smartru.common.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    @Query("SELECT u FROM User u WHERE u.login = :login")
    Optional<User> findByUsername(@Param("login") String login);

    @Modifying
    @Transactional
    @Query("UPDATE User SET accessToken=:accessToken," +
            "refreshToken=:refreshToken " +
            "WHERE id=:userId")
    void onlyTokensUpdate(@Param("userId") long userId,
                          @Param("accessToken") String accessToken,
                          @Param("refreshToken") String refreshToken);
}
