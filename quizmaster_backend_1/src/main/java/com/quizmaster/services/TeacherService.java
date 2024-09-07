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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherService {

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseModel> registerTeacher(RegisterTeacherRequestModel registerRequestModel) {
        // Validate the input request
        if (registerRequestModel == null || registerRequestModel.getEmail() == null) {
            return ResponseEntity.badRequest().body(
                    RegisterResponseModel.builder()
                            .userId(null)
                            .message("Invalid registration request")
                            .build()
            );
        }

        // Check if the user is already registered
        Optional<User> existingUser = usersRepository.findByEmail(registerRequestModel.getEmail());
        if (!existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RegisterResponseModel.builder()
                            .userId(null)
                            .message("User not found. Please register as a user first.")
                            .build()
            );
        }

        User user = existingUser.get();

        // Check if the user is already registered as a teacher
        Optional<Teacher> existingTeacher = teacherRepository.findByUser(user);
        if (existingTeacher.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    RegisterResponseModel.builder()
                            .userId(user.getKey())
                            .message("User is already registered as a teacher")
                            .build()
            );
        }else {
            user.setStatus("active");
            user.setSchool(registerRequestModel.getSchool());
            // Register the user as a teacher
            Teacher newTeacher = new Teacher();
            newTeacher.setUser(user);
            newTeacher.setTeacherID(MyUtils.generateTeacherID());

            teacherRepository.save(newTeacher);  // Save Teacher to repository

            // Send welcome email to the teacher
            sendWelcomeEmail(user, newTeacher);

            // Return success response
            RegisterResponseModel response = RegisterResponseModel.builder()
                    .userId(user.getKey())  // Changed to `getId()` assuming `user.getKey()` is incorrect
                    .message("Teacher Registration Successful")
                    .build();

            return ResponseEntity.ok(response);
        }



    }

    private void sendWelcomeEmail(User user, Teacher newTeacher) {
        String subject = "Welcome to QuizMaster!";
        String text = "Dear " + user.getFirstName() + ",\n\n\n" +
                "Welcome to the QuizMaster platform!\n\n" +
                "We are thrilled to have you join our community of educators. As a teacher, you play a crucial role in shaping the future, and we’re excited to support you on this journey.\n\n"+
                "Your registration as a teacher is successful. Your Teacher ID is: " + newTeacher.getTeacherID() + ".\n" +
                "To get started, you can log in to your account "+ user.getEmail() + " " +
                "and explore your dashboard. If you have any questions or need assistance, please don’t hesitate to reach out to our support team at quizmastercamp_support@gmail.com or visit our help center QuizMasterHelp.\n\n"+
                "Best regards,\nThe QuizMaster Team";
        emailService.sendRegistrationEmail(user.getEmail(), subject, text);
    }
}