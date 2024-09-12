package com.quizmaster;

import com.quizmaster.services.EmailService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmailServiceTest {

    @Mock
    private JavaMailSender emailSender;

    @InjectMocks
    private EmailService emailService;

    @Test
    public void testSendRegistrationEmail() {
        // Arrange
        String to = "test@example.com";
        String subject = "Registration";
        String text = "Welcome to QuizMaster!";
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        message.setFrom("noreply@quizmaster.com");

        // Act
        emailService.sendRegistrationEmail(to, subject, text);

        // Assert
        verify(emailSender).send(any(SimpleMailMessage.class));
    }

    @Test
    public void testSendRegistrationEmailHandlesException() {
        // Arrange
        String to = "test@example.com";
        String subject = "Registration";
        String text = "Welcome to QuizMaster!";
        when(emailSender.send(any(SimpleMailMessage.class))).thenThrow(new RuntimeException("Mail sending failed"));

        emailService.sendRegistrationEmail(to, subject, text);


    }
}
