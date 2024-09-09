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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private UsersService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    public ResponseEntity<RegisterResponseModel> registerStudent(RegisterStudentRequestModel registerRequestModel) {
        User currentUser = userService.currentUser();

        if (currentUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
            if (registerRequestModel == null || registerRequestModel.getEmail() == null) {
                return ResponseEntity.badRequest().body(
                        RegisterResponseModel.builder()
                                .userId(null)
                                .message("Invalid registration request")
                                .build()
                );
            } else {

                // Generate a random password
                String password = MyUtils.generateRandomPassword();

                // Create a new user for the student
                User newUser = User.builder()
                        .email(registerRequestModel.getEmail())
                        .firstName(registerRequestModel.getFirstName())
                        .lastName(registerRequestModel.getLastName())
                        .role("student")
                        .status("active")
                        .password(passwordEncoder.encode(password))
                        .build();

                // Save the new user to the database
                usersRepository.saveAndFlush(newUser);  // Save the user before creating a Student

                // Create a new student
                Student newStudent = new Student();
                newStudent.setUser(newUser);  // Link the user to the student
                newStudent.setStudentID(MyUtils.generateStudentID());
                newStudent.setGrade(registerRequestModel.getGrade());
                newStudent.setDateOfBirth(registerRequestModel.getDateOfBirth());
                newStudent.setAddress(registerRequestModel.getAddress());
                newStudent.setRegistrationDate(registerRequestModel.getRegistrationDate());
                newStudent.setParentName(registerRequestModel.getParentName());
                newStudent.setParentContact(registerRequestModel.getParentContact());

                Teacher assignedTeacher = null;

                // Check if the current user is a teacher
                if (Objects.equals(currentUser.getRole(), "teacher")) {
                    // Fetch the teacher based on the current user's email
                    Optional<Teacher> teacherOpt = teacherRepository.findByUserEmail(currentUser.getEmail());

                    if (teacherOpt.isPresent()) {
                        assignedTeacher = teacherOpt.get();  // Assign the current user (if teacher) as the student's teacher
                    }
                }

                // If no teacher found (current user is not a teacher or no teacher exists), set the teacher as "Not assigned"
                if (assignedTeacher == null) {
                    newStudent.setTeacher(null);  // Mark teacher as "Not assigned"
                } else {
                    newStudent.setTeacher(assignedTeacher);  // Assign the teacher to the student
                }

                // Save the student to the database
                studentRepository.save(newStudent);

                // Send the welcome email
                sendWelcomeEmail(newUser, newStudent, password);

                // Return success response
                RegisterResponseModel response = RegisterResponseModel.builder()
                        .userId(newUser.getKey())  // Use newUser's ID
                        .message("Student Registration Successful")
                        .build();

                return ResponseEntity.ok(response);
            }
        }
    }

    private void sendWelcomeEmail(User user, Student newStudent, String password) {
        String subject = "Welcome to QuizMaster!";
        String text = "Dear " + user.getFirstName() + ",\n\n" +
                "Welcome to the QuizMaster platform!\n\n" +
                "We are thrilled to have you join our learning community. As a student, you are about to embark on an exciting journey of knowledge and discovery.\n\n" +
                "Your registration as a student is successful. Your Student ID is: " + newStudent.getStudentID() + ".\n\n" +
                "Log in to your account to explore your dashboard. \n\n" +
                "Email: " + user.getEmail() + "\n" +
                "Password: " + password + "\n\n" +
                "Explore quizzes, lessons, and other features designed to help you learn and grow. You can also track your progress, connect with your teacher, and challenge yourself with quizzes in various subjects.\n\n" +
                "If you have any questions or need assistance, please don’t hesitate to reach out to our support team at quizmastercamp_support@gmail.com or visit our help center QuizMasterHelp.\n\n" +
                "Best regards,\nThe QuizMaster Team";

        emailService.sendRegistrationEmail(user.getEmail(), subject, text);
    }
}
