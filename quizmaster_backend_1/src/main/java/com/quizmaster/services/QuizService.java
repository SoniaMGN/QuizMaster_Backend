package com.quizmaster.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quizmaster.entities.Question;
import com.quizmaster.entities.Quiz;
import com.quizmaster.entities.Summary;
import com.quizmaster.entities.User;
import com.quizmaster.models.*;
import com.quizmaster.repositories.QuizRepository;
import com.quizmaster.repositories.SummariesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepository;

    @Autowired
    private UsersService userService;

    public List<Question> createQuestions( String Json)
    {
        ObjectMapper objectMapper = new ObjectMapper();
        List<Question> questions = new ArrayList<>();

        try {
            Question[] questionsArray = objectMapper.readValue(Json, Question[].class);
            questions.addAll(Arrays.asList(questionsArray));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return questions;
    }


    public Quiz getQuiz(Long id) {
        return quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found with id " + id));
    }
}
