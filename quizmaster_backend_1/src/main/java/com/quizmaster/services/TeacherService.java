package com.quizmaster.services;

import com.quizmaster.entities.Teacher;
import com.quizmaster.entities.User;
import com.quizmaster.models.RegisterResponseModel;
import com.quizmaster.models.RegisterTeacherRequestModel;
import com.quizmaster.repositories.TeacherRepository;
import com.quizmaster.repositories.UsersRepository;
import com.quizmaster.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseModel> registerUser(RegisterTeacherRequestModel registerRequestModel) {

        if (registerRequestModel == null) {
            return ResponseEntity.badRequest().body(
                    RegisterResponseModel.builder()
                            .userId(null)
                            .message("Invalid registration request")
                            .build()
            );
        }

        Optional<User> existingUser = usersRepository.findByEmail(registerRequestModel.getEmail());
        if (existingUser.isPresent()) {

            User newUser = User.builder()
                    .school(registerRequestModel.getSchool())
                    .status("active")
                    .build();

            usersRepository.saveAndFlush(newUser);

            Teacher newTeacher = new Teacher();
            newTeacher.setUser(newUser);
            newTeacher.setTeacherID(MyUtils.generateRandomID());

            teacherRepository.save(newTeacher);  // Save Teacher to repository

            String subject = "Welcome to QuizMaster!";
            String text = "Dear " + newUser.getFirstName() + ",\n\n" +
                    "Your registration as a teacher is successful. Your Teacher ID is: " + newTeacher.getTeacherID() + ".\n" +
                    "Welcome to the QuizMaster platform!\n\n" +
                    "Best regards,\nThe QuizMaster Team";
            emailService.sendRegistrationEmail(newUser.getEmail(), subject, text);

            RegisterResponseModel response = RegisterResponseModel.builder()
                    .userId(newUser.getKey())
                    .message("Registration Successful")
                    .build();

            return ResponseEntity.ok(response);

        }

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                RegisterResponseModel.builder()
                        .userId(null)
                        .message("Email already Registered")
                        .build()
        );
    }
}
