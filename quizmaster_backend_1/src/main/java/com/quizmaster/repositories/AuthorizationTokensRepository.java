package com.quizmaster.repositories;

import com.quizmaster.entities.AuthorizationTokens;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AuthorizationTokensRepository extends JpaRepository<AuthorizationTokens,Long> {


    @Query("select a from AuthorizationTokens a where a.value=?1")
    Optional<AuthorizationTokens> findByValue(String value);
}

