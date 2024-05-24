package com.quizmaster.controllers;

import com.quizmaster.entities.Quiz;
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
public class ScoreController {
}
