package com.quizmaster.services;

import com.quizmaster.entities.Student;
import com.quizmaster.entities.Teacher;
import com.quizmaster.entities.User;
import com.quizmaster.models.RegisterResponseModel;
import com.quizmaster.models.RegisterStudentRequestModel;
import com.quizmaster.repositories.StudentRepository;
import com.quizmaster.repositories.TeacherRepository;
import com.quizmaster.repositories.UsersRepository;
import com.quizmaster.utils.MyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UsersService userService;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseModel> registerStudent(RegisterStudentRequestModel registerRequestModel) {
        User currentUser = userService.currentUser();

        if (currentUser == null) {
            logger.warn("Unauthorized access attempt.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        if (registerRequestModel == null || registerRequestModel.getEmail() == null) {
            return ResponseEntity.badRequest().body(
                    RegisterResponseModel.builder()
                            .userId(null)
                            .message("Invalid registration request")
                            .build()
            );
        }

        Optional<User> existingUser = usersRepository.findByEmail(registerRequestModel.getEmail());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    RegisterResponseModel.builder()
                            .userId(null)
                            .message("User already registered.")
                            .build()
            );
        }

        String password = MyUtils.generateRandomPassword();
        User user = User.builder()
                .email(registerRequestModel.getEmail())
                .firstName(registerRequestModel.getFirstName())
                .lastName(registerRequestModel.getLastName())
                .role("student")
                .status("active")
                .school(registerRequestModel.getSchool())
                .password(passwordEncoder.encode(password))
                .build();

        usersRepository.save(user);


        Optional<User> newUser = usersRepository.findById(user.getKey());
        if (newUser.isPresent()) {
            User userNew = newUser.get();

            Student newStudent = new Student();
            newStudent.setUser(userNew);  // Link the User to the student
            newStudent.setStudentID(MyUtils.generateStudentID());
            newStudent.setGrade(registerRequestModel.getGrade());
            newStudent.setDateOfBirth(registerRequestModel.getDateOfBirth());
            newStudent.setAddress(registerRequestModel.getAddress());
            newStudent.setParentName(registerRequestModel.getParentName());
            newStudent.setParentContact(registerRequestModel.getParentContact());

            Teacher assignedTeacher = null;

            // If the current user is a teacher, assign them to the student
            if ("teacher".equals(currentUser.getRole())) {
                Optional<Teacher> teacherOpt = teacherRepository.findByUserEmail(currentUser.getEmail());
                if (teacherOpt.isPresent()) {
                    assignedTeacher = teacherOpt.get();
                }
            }

            newStudent.setTeacher(assignedTeacher);

            try {
                studentRepository.save(newStudent);
            } catch (Exception e) {
                logger.error("Error saving student to the database", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                        RegisterResponseModel.builder()
                                .userId(user.getKey())
                                .message("Error saving student")
                                .build()
                );
            }

            sendWelcomeEmail(user, newStudent, password);

            // Return success response
            RegisterResponseModel response = RegisterResponseModel.builder()
                    .userId(user.getKey())  // Return the user's key (ID)
                    .message("Student Registration Successful")
                    .build();

            return ResponseEntity.ok(response);
        } else {
            logger.error("User not found after saving");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                    RegisterResponseModel.builder()
                            .userId(null)
                            .message("Error finding registered user")
                            .build()
            );
        }
    }

    private void sendWelcomeEmail(User user, Student newStudent, String password) {
        String subject = "Welcome to QuizMaster!";
        String text = String.format("Dear %s %s,\n\n" +
                        "Welcome to the QuizMaster platform!\n\n" +
                        "We are thrilled to have you join our learning community. As a student, you are about to embark on an exciting journey of knowledge and discovery.\n\n" +
                        "Your registration as a student is successful. Your Student ID is: %s.\n\n" +
                        "Log in to your account to explore your dashboard. \n\n" +
                        "Email: %s\n" +
                        "Password: %s\n\n" +
                        "Explore quizzes, lessons, and other features designed to help you learn and grow. You can also track your progress, connect with your teacher, and challenge yourself with quizzes in various subjects.\n\n" +
                        "If you have any questions or need assistance, please don’t hesitate to reach out to our support team at quizmastercamp_support@gmail.com or visit our help center QuizMasterHelp.\n\n" +
                        "Best regards,\nThe QuizMaster Team",
                user.getFirstName(), user.getLastName(), newStudent.getStudentID(),
                user.getEmail(), password);

        emailService.sendRegistrationEmail(user.getEmail(), subject, text);
    }
}
