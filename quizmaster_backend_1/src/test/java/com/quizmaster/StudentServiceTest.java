package com.quizmaster;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.quizmaster.entities.Student;
import com.quizmaster.entities.User;
import com.quizmaster.models.RegisterResponseModel;
import com.quizmaster.models.RegisterStudentRequestModel;
import com.quizmaster.repositories.StudentRepository;
import com.quizmaster.services.EmailService;
import com.quizmaster.services.StudentService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class StudentServiceTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private StudentService studentService;

    @Test
    public void testRegisterStudent_successful() {
        // Create the User object
        User user = new User("student@example.com", "Jane", "Doe", "password");

        // Use the builder pattern to create RegisterStudentRequestModel
        RegisterStudentRequestModel request = RegisterStudentRequestModel.builder()
                .email("student@example.com")
                .school("Sample School")
                .firstName("John")
                .lastName("Doe")
                .grade("2")
                .dateOfBirth("2016-05-15")
                .address("123 Main St")
                .parentName("Jane Doe")
                .parentContact("123-456-7890")
                .build();

        // Mock the repository save call
        when(studentRepository.save(any(Student.class))).thenReturn(new Student());

        // Call the registerStudent method in the service
        ResponseEntity<RegisterResponseModel> response = studentService.registerStudent(request);

        // Assert that the response is OK and that the registration was successful
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Student Registration Successful", response.getBody().getMessage());

        // Verify that the email service was called
        verify(emailService).sendRegistrationEmail(eq(user.getEmail()), anyString(), anyString());
    }
}
