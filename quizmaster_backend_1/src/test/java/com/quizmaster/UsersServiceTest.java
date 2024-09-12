package com.quizmaster;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.quizmaster.entities.User;
import com.quizmaster.models.RegisterRequestModel;
import com.quizmaster.models.RegisterResponseModel;
import com.quizmaster.repositories.UsersRepository;
import com.quizmaster.services.UsersService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

public class UsersServiceTest {

    @Mock
    private UsersRepository usersRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UsersService usersService;

    // Test for when an email is already registered
    @Test
    public void testRegisterUser_whenEmailAlreadyRegistered() {
        // Use builder to create RegisterRequestModel
        RegisterRequestModel request = RegisterRequestModel.builder()
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        // Mock existing user
        when(usersRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(new User()));

        // Call service
        ResponseEntity<RegisterResponseModel> response = usersService.registerUser(request);

        // Assert HTTP status and message
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Email already Registered", response.getBody().getMessage());
    }

    // Test for successful registration
    @Test
    public void testRegisterUser_successful() {
        // Use builder to create RegisterRequestModel
        RegisterRequestModel request = RegisterRequestModel.builder()
                .email("john@example.com")
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .build();

        // Mock no existing user
        when(usersRepository.findByEmail(request.getEmail())).thenReturn(Optional.empty());

        // Mock password encoding
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPassword");

        // Call service
        ResponseEntity<RegisterResponseModel> response = usersService.registerUser(request);

        // Assert HTTP status and message
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration Successful", response.getBody().getMessage());

        // Verify password encoding was called
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    // Test when no user is authorized
    @Test
    public void testGetCurrentUser_unauthorized() {
        // Mock SecurityContextHolder to return null or empty Authentication
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(null);
        SecurityContextHolder.setContext(securityContext);

        // Call service
        ResponseEntity<User> response = usersService.getCurrentUser();

        // Assert HTTP status
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // Test when a user is authorized
    @Test
    public void testGetCurrentUser_authorized() {
        User user = new User("john@example.com", "John", "Doe", "password");

        // Mock SecurityContextHolder to return a valid Authentication
        Authentication authentication = mock(Authentication.class);
        when(authentication.getName()).thenReturn(user.getEmail());

        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Mock the repository to return the user
        when(usersRepository.findByEmail(anyString())).thenReturn(Optional.of(user));

        // Call service
        ResponseEntity<User> response = usersService.getCurrentUser();

        // Assert HTTP status and user details
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("john@example.com", response.getBody().getEmail());
    }
}
