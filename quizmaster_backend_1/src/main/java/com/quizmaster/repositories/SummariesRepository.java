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


    @Query(value = "select a.key,a.title,a.date,a.summary from summaries a where a.user_key=?1",nativeQuery = true)
    List<Summary> findByUserID(Long userKey);
}

