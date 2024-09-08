package com.quizmaster.controllers;

import com.quizmaster.models.QuizHighestScoreModel;
import com.quizmaster.models.ScoreRequestModel;
import com.quizmaster.models.ScoreResponseModel;
import com.quizmaster.services.ScoreService;
import com.quizmaster.services.UsersService;
import com.quizmaster.services.QuizService;
import com.quizmaster.entities.Score;
import com.quizmaster.services.ScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/api/v1/scores")
public class ScoreController {

    @Autowired
    private ScoreService scoreService;

    @PostMapping("saveScore")
    ResponseEntity<?> saveScore(@RequestBody @Valid ScoreRequestModel scoreRequestModel)
    {
        scoreService.saveScore(scoreRequestModel);
        try {
            scoreService.saveScore(scoreRequestModel);
            return ResponseEntity.ok("Score saved successfully");
        } catch (Exception e) {
            e.printStackTrace(); // Log the exception
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving score");
        }
    }

    @GetMapping("getScores/{quizId}")
    public List<Score> getScoresByQuizId(@PathVariable Long quizId) {
        return scoreService.getScoresByQuizId(quizId);
    }



    @GetMapping("/highest")
    public ResponseEntity<List<QuizHighestScoreModel>> getHighestScoresForAllQuizzes() {
        List<QuizHighestScoreModel> highestScores = scoreService.getHighestScoresForAllQuizzes();
        return ResponseEntity.ok(highestScores);
    }
}
