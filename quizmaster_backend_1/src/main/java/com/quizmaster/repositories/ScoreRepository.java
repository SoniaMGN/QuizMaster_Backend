package com.quizmaster.repositories;

import com.quizmaster.entities.Quiz;
import com.quizmaster.entities.Score;
import com.quizmaster.entities.Summary;
import com.quizmaster.models.QuizHighestScoreModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public interface ScoreRepository extends JpaRepository<Score, Long> {

    @Query("SELECT s.quiz.title AS quizTitle, MAX(s.score) AS highestScore FROM Score s GROUP BY s.quiz.title")
    List<Object[]> findHighestScoresForEachQuizRaw();

    default List<QuizHighestScoreModel> findHighestScoresForEachQuiz() {
        List<Object[]> results = findHighestScoresForEachQuizRaw();
        List<QuizHighestScoreModel> highestScores = new ArrayList<>();
        for (Object[] result : results) {
            QuizHighestScoreModel scoreModel = new QuizHighestScoreModel();
            scoreModel.setQuizTitle((String) result[0]);
            scoreModel.setHighestScore((Integer) result[1]);
            highestScores.add(scoreModel);
        }
        return highestScores;
    }

    @Query("SELECT s FROM Score s WHERE s.quiz.key = ?1")
    List<Score> findByQuizID(Long quizId);
}


