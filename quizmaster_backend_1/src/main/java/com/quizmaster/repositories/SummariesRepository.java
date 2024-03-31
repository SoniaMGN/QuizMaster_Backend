package com.quizmaster.repositories;

import com.quizmaster.entities.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SummariesRepository extends JpaRepository<Summary, Long> {


    @Transactional
    @Modifying
    @Query(value = "INSERT INTO summaries (date, title, summary, user_id) " +
            "VALUES (:#{#summary.date}, :#{#summary.title}, :#{#summary.summary}, :#{#summary.user.id})",
            nativeQuery = true)
    void addSummary(Summary summary);
}

