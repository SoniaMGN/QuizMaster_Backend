package com.quizmaster;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.quizmaster.entities.*;
import com.quizmaster.repositories.*;
import com.quizmaster.services.*;
import com.quizmaster.models.*;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class TeacherServiceTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private TeacherService teacherService;

    @Test
    public void testRegisterTeacher_successful() {
        User user = new User("teacher@example.com", "John", "Doe", "password");

        // Using the builder pattern as defined in the RegisterTeacherRequestModel class
        RegisterTeacherRequestModel request = RegisterTeacherRequestModel.builder()
                .email("john@example.com")
                .gradeLevel("10")
                .homeroomClass("10A")
                .yearsOfExperience(5)
                .classroomNumber("C102")
                .password("password123")
                .build();

        when(teacherRepository.save(any(Teacher.class))).thenReturn(new Teacher());

        ResponseEntity<RegisterResponseModel> response = teacherService.registerTeacher(request);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Teacher Registration Successful", response.getBody().getMessage());

        verify(emailService).sendRegistrationEmail(eq(user.getEmail()), anyString(), anyString());
    }
}
