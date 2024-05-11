package com.quizmaster.controllers;

import com.quizmaster.entities.Quiz;
import com.quizmaster.entities.Question;
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

@RestController
@RequestMapping("/api/v1/quiz")
public class QuizController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private QuizRepository quizRepository;

/*    private List<Question> mapToQuestionEntities(@NotEmpty(message = "Question list cannot be empty") List<Question> questionRequestModels) {
        List<Question> questions = new ArrayList<>();
        for (Question questionRequestModel : questionRequestModels) {
            Question question = mapToQuestionEntity(questionRequestModel);
            questions.add(question);
        }
        return questions;
    }


    private Question mapToQuestionEntity(Question questionRequestModel) {
        Question question = new Question();
        question.setQuestion(questionRequestModel.getQuestion());
        question.setAnswer(questionRequestModel.getAnswer());
        question.setOption_1(questionRequestModel.getOption_1());
        question.setOption_2(questionRequestModel.getOption_2());
        question.setOption_3(questionRequestModel.getOption_3());
        question.setOption_4(questionRequestModel.getOption_4());
        question.setQuiz(questionRequestModel.getQuiz());
        return question;
    }*/

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

}




