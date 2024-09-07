package com.quizmaster.services;


import com.quizmaster.entities.*;
import com.quizmaster.models.*;
import com.quizmaster.repositories.*;
import com.quizmaster.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateRequestCustomizer;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.dao.DataIntegrityViolationException;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Service
public class UsersService {
    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private EmailService emailService;

    public String registerUser(String email, String firstName, String lastName, String password) {
        User user = User.builder().email(email).firstName(firstName).lastName(lastName).password(password).build();

        usersRepository.save(user);

        return "User saved Successfully. User : " + user;
    }

    public List<User> listUsers() {
        return usersRepository.findAll();
    }


    public ResponseEntity<RegisterResponseModel> registerUser(RegisterRequestModel registerRequestModel) {

        Optional<User> userOptional = usersRepository.findByEmail(registerRequestModel.getEmail());
        if (userOptional.isPresent()) {
            RegisterResponseModel response = RegisterResponseModel.builder()
                    .userId(null)
                    .message("Email already Registered")
                    .build();

            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        User superuser = User.builder()
                .email(registerRequestModel.getEmail())
                .firstName(registerRequestModel.getFirstName())
                .lastName(registerRequestModel.getLastName())
                .role("admin") // You can set the role to "admin" or something similar
                .status("active")
                .password(passwordEncoder.encode(registerRequestModel.getPassword()))
                .isSuperuser(true) // Set as superuser
                .build();

        usersRepository.saveAndFlush(superuser);

        RegisterResponseModel response = RegisterResponseModel.builder()
                .userId(superuser.getKey())
                .message("Registration Successful")
                .build();

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<User> getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User currUser = optionalUser.get();
            return ResponseEntity.ok(currUser);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

    }

    public User currentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> optionalUser = usersRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            User currUser = optionalUser.get();
            return currUser;
        } else {
            return null;
        }
    }

    public ResponseEntity<String> updateUser(UpdateUserRequestModel updateUserRequestModel) {
        User myUser = currentUser();
        if (myUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        myUser.setFirstName(updateUserRequestModel.getFirstName());
        myUser.setLastName(updateUserRequestModel.getLastName());

        usersRepository.save(myUser);

        return ResponseEntity.ok("Updated Successfully");
    }

    public ResponseEntity<String> changePassword(ChangePasswordRequestModel changePasswordRequestModel) {
        User myUser = currentUser();
        if (myUser == null)
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);

        if (!changePasswordRequestModel.getPassword().equals(changePasswordRequestModel.getConfirmPassword()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Passwords Dont Match");

        myUser.setPassword(passwordEncoder.encode(changePasswordRequestModel.getPassword()));

        usersRepository.save(myUser);

        return ResponseEntity.ok("Password Changed Successfully");
    }

    public ResponseEntity<RegisterResponseModel> registerTeacher(TeacherRegisterRequestModel registerRequestModel) {

        if (currentUser() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        } else {
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
            if (existingUser.isPresent()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        RegisterResponseModel.builder()
                                .userId(null)
                                .message("User already registered.")
                                .build()
                );
            } else {
                if (currentUser().isSuperuser()) {
                    User newUser = User.builder()
                            .email(registerRequestModel.getEmail())
                            .firstName(registerRequestModel.getFirstName())
                            .lastName(registerRequestModel.getLastName())
                            .role("teacher")
                            .status("inactive")
                            .school(registerRequestModel.getSchool())
                            .build();

                    usersRepository.save(newUser);
                } else {
                    return ResponseEntity.badRequest().body(
                            RegisterResponseModel.builder()
                                    .userId(null)
                                    .message("Current user is not authorized to register a teacher")
                                    .build()
                    );
                }
            }


        }


        return null;
    }

    public ResponseEntity<RegisterResponseModel> registerStudent(RegisterStudentRequestModel registerRequestModel) {

        if (currentUser() == null) {
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

                usersRepository.saveAndFlush(newUser);

                // Create a new student
                Student newStudent = new Student();
                newStudent.setStudentID(MyUtils.generateStudentID());
                newStudent.setGrade(registerRequestModel.getGrade());
                newStudent.setDateOfBirth(registerRequestModel.getDateOfBirth());
                newStudent.setAddress(registerRequestModel.getAddress());
                newStudent.setRegistrationDate(registerRequestModel.getRegistrationDate());
                newStudent.setParentName(registerRequestModel.getParentName());
                newStudent.setParentContact(registerRequestModel.getParentContact());

                Teacher assignedTeacher = null;

                // Check if the current user is a teacher
                if (Objects.equals(currentUser().getRole(), "teacher")) {
                    // Fetch the teacher based on the current user's email
                    Optional<Teacher> teacherOpt = teacherRepository.findByUserEmail(currentUser().getEmail());

                    if (teacherOpt.isPresent()) {
                        assignedTeacher = teacherOpt.get(); // Assign the current user (if teacher) as the student's teacher
                    }
                }

                // If no teacher found (current user is not a teacher or no teacher exists), set the teacher as "Not assigned"
                if (assignedTeacher == null) {
                    newStudent.setTeacher(null); // Mark teacher as "Not assigned"
                } else {
                    newStudent.setTeacher(assignedTeacher); // Assign the teacher to the student
                }

                // Save the student
                studentRepository.save(newStudent);

                // Send the welcome email
                sendWelcomeEmail(newUser, newStudent, password);

                // Return success response
                RegisterResponseModel response = RegisterResponseModel.builder()
                        .userId(currentUser().getKey())
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
