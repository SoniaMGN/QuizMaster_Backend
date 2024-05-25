package com.quizmaster.services;

import com.quizmaster.entities.Quiz;
import com.quizmaster.entities.Score;
import com.quizmaster.entities.User;
import com.quizmaster.models.QuizHighestScoreModel;
import com.quizmaster.models.ScoreRequestModel;
import com.quizmaster.repositories.ScoreRepository;
import com.quizmaster.repositories.QuizRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

@Service
public class ScoreService {

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private QuizService quizService;

    @Autowired
    private UsersService usersService;

    public List<Score> getScoresByQuizId(Long quizId) {
        return scoreRepository.findByQuizID(quizId);
    }

    public ResponseEntity<?> saveScore(@RequestBody @Valid ScoreRequestModel scoreRequestModel) {
        User myUser = usersService.currentUser();
        if (myUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            Quiz quiz = quizService.getQuiz(scoreRequestModel.getQuizID());

            Score score = Score.builder()
                    .score(scoreRequestModel.getScore())
                    .quiz(quiz)
                    .build();
            scoreRepository.save(score);

            return ResponseEntity.ok("Score Saved");
        }
    }


    public List<QuizHighestScoreModel> getHighestScoresForAllQuizzes() {
        return scoreRepository.findHighestScoresForEachQuiz();
    }
}
