package com.quizmaster.repositories;

import com.quizmaster.entities.Score;
import com.quizmaster.entities.Summary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ScoreRepository extends JpaRepository<Score, Long> {


}
