package com.quizmaster.repositories;

import com.quizmaster.entities.Quiz;
import com.quizmaster.entities.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface QuizRepository extends JpaRepository<Quiz, Long> {

    @Query(value = "select a from quiz a where a.user.key=?1")
    List<Quiz> findByUserID(Long userKey);

    @Query(value = "SELECT a FROM quiz a WHERE a.user.key = ?1 AND a.title = ?2")
    Quiz findByTitleAndUserKey(Long userKey, String title);
}
