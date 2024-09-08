package com.quizmaster.controllers;

import com.quizmaster.entities.Quiz;
import com.quizmaster.entities.Question;
import com.quizmaster.entities.Summary;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.QuizRepository;
import com.quizmaster.services.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private QuizRepository quizRepository;

    @PostMapping("/saveQuiz")
    public ResponseEntity<?> saveQuiz(@RequestBody @Valid QuizRequestModel quizRequestModel) {
        User myUser = usersService.currentUser();
        if (myUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            List<Question> questions = quizRequestModel.getQuestionList();
            Quiz newQuiz = Quiz.builder()
                    .title(quizRequestModel.getTitle())
                    .questions(questions)
                    .user(myUser)
                    .build();

            for (Question q : questions) {
                q.setQuiz(newQuiz);
            }

            // Set the quiz for each question
            questions.forEach(question -> question.setQuiz(newQuiz));

            quizRepository.save(newQuiz);

            QuizResponseModel response = QuizResponseModel.builder()
                    .quizId(newQuiz.getKey())
                    .message("Quiz saved successfully!")
                    .questions(newQuiz.getQuestions())
                    .build();
            return ResponseEntity.ok(response);
        }
    }

    @GetMapping("/myQuizzes")
    public ResponseEntity<List<Quiz>> myQuizzes() {
        User myUser = usersService.currentUser();

        return ResponseEntity.ok(myUser.getQuizzes());
    }

    @GetMapping("/myQuiz")
    public ResponseEntity<QuizResponseModel> getQuiz(@RequestParam("title") String title) {
        User myUser = usersService.currentUser();
        if (myUser == null)
        {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        else {

            Quiz quiz = quizRepository.findByTitleAndUserKey(myUser.getKey(), title);

            QuizResponseModel response = QuizResponseModel.builder()
                    .quizId(quiz.getKey())
                    .questions(quiz.getQuestions())
                    .message("Successfully retrieved quiz")
                    .build();
            return ResponseEntity.ok(response);
        }

    }


    @DeleteMapping("/deleteQuiz/{quizKey}")
    public ResponseEntity<String> deleteSummary(@PathVariable("quizKey") Long quizKey) {
        User myUser = usersService.currentUser();

        if (myUser != null) {
            Optional<Quiz> myquiz = quizRepository.findById(quizKey);
            if (myquiz.isPresent()) {
                Quiz quiz = myquiz.get();
                if (quiz.getUser().getKey().equals(myUser.getKey())) {
                    quizRepository.delete(quiz);

                    return ResponseEntity.ok("Quiz deleted");

                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("quiz does not belong to user");
                }


            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("quiz found");

            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("user not authorised");
    }



}
