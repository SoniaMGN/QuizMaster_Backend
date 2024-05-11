package com.quizmaster.repositories;

import com.quizmaster.entities.Summary;
import com.quizmaster.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;

@Repository
public interface SummariesRepository extends JpaRepository<Summary, Long> {


    @Query(value = "select a from summaries a where a.user.key=?1")
    List<Summary> findByUserID(Long userKey);

    @Query(value = "SELECT a FROM summaries a WHERE a.user.key = ?1 AND a.title = ?2")
    Summary findByTitleAndUserKey(Long userKey, String title);
}

